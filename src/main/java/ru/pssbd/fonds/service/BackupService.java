package ru.pssbd.fonds.service;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.pssbd.fonds.utils.SSHUtils;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class BackupService {

    @Value("${app.db.primary.host}")
    private String primaryHost;

    @Value("${app.db.primary.port}")
    private int primaryPort;

    @Value("${app.db.primary.name}")
    private String primaryDbName;

    @Value("${app.db.primary.user}")
    private String primaryUser;

    @Value("${app.db.primary.password}")
    private String primaryPassword;

    @Value("${app.backup.pg-dump-path}")
    private String pgDumpPath;

    @Value("${app.backup.local-dump-dir}")
    private String localDumpDir;

    @Value("${app.ssh.remote.host}")
    private String sshHost;

    @Value("${app.ssh.remote.port}")
    private int sshPort;

    @Value("${app.ssh.remote.user}")
    private String sshUser;

    @Value("${app.ssh.remote.private-key-path:}")
    private String sshKeyPath;

    @Value("${app.ssh.remote.password:}")
    private String sshPassword;

    @Value("${app.backup.remote-dump-dir}")
    private String remoteDumpDir;

    @Value("${app.db.remote.user}")
    private String remoteDbUser;

    @Value("${app.db.remote.password}")
    private String remoteDbPassword;

    @Value("${app.db.remote.name}")
    private String remoteDbName;

    @Value("${app.db.remote.host}")
    private String remoteDbHost;  // обычно "localhost" на remote
    @Value("${app.db.remote.port}")
    private int remoteDbPort;     // обычно 5432

    @Value("${app.backup.schedule-cron}")
    private String scheduleCron;

    @Value("${app.db.remote.container-id}")
    private String containerId;

    @Value("${app.db.remote.use-sudo-docker:false}")
    private boolean useSudoDocker;

    private SSHUtils sshUtils;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    @PostConstruct
    public void init() throws IOException {
        // Инициализируем SSHUtils
        sshUtils = new SSHUtils(sshUser, sshHost, sshPort, sshKeyPath, sshPassword);
        // Убедимся, что локальная директория существует
        Path localDir = Paths.get(localDumpDir);
        if (!Files.exists(localDir)) {
            Files.createDirectories(localDir);
        }
    }

    /**
     * Запуск по расписанию. cron-выражение из свойств.
     */
    @Scheduled(cron = "${app.backup.schedule-cron}")
    public void scheduledBackupAndRestore() {
        try {
            performBackupAndRestore();
        } catch (Exception e) {
            System.err.println("[BackupService] Ошибка при scheduledBackupAndRestore: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void performBackupAndRestore() throws Exception {
        // Шаг 0: проверка доступности локальной базы данных
        if (!isLocalDbAvailable()) {
            return;
        }
        // Шаг 1: сделать дамп
        String dumpFileName = createDump();
        // Шаг 2: переслать дамп на remote
        String remoteDumpPath = sendDumpToRemote(dumpFileName);
        // Шаг 3: на remote выполнить восстановление
        restoreOnRemote(remoteDumpPath);
        // Шаг 4: опционально удалить дампы
        cleanupLocalDump(dumpFileName);
        cleanupRemoteDump(remoteDumpPath);
    }

    /**
     * Создаёт дамп и возвращает имя файла (без пути).
     */
    private String createDump() throws IOException, InterruptedException {
        // Формируем имя: fonds_YYYYMMDD_HHMMSS.dump
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String fileName = primaryDbName + "_" + timestamp + ".dump";
        Path dumpPath = Paths.get(localDumpDir, fileName);
        // Команда: pg_dump.exe -h host -p port -U user -F c -b -v -f dumpPath dbname
        ProcessBuilder pb = new ProcessBuilder(
                pgDumpPath,
                "-h", primaryHost,
                "-p", String.valueOf(primaryPort),
                "-U", primaryUser,
                "-F", "c",
                "-b",
                "-v",
                "-f", dumpPath.toString(),
                primaryDbName
        );
        // Передаём пароль в env для pg_dump
        pb.environment().put("PGPASSWORD", primaryPassword);
        pb.redirectErrorStream(true);
        Process proc = pb.start();
        // Логируем вывод pg_dump
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("[pg_dump] " + line);
            }
        }
        int exit = proc.waitFor();
        if (exit != 0) {
            throw new IOException("pg_dump завершился с кодом " + exit);
        }
        System.out.println("[BackupService] Локальный дамп создан: " + dumpPath);
        return fileName;
    }

    /**
     * Пересылает дамп на remote, возвращает полный путь на remote.
     */
    private String sendDumpToRemote(String dumpFileName) throws JSchException, SftpException {
        Path localPath = Paths.get(localDumpDir, dumpFileName);
        String remotePath = remoteDumpDir.endsWith("/") ?
                remoteDumpDir + dumpFileName :
                remoteDumpDir + "/" + dumpFileName;
        // Передаём файл
        sshUtils.scpToRemote(localPath.toString(), remotePath);
        return remotePath;
    }

    private void restoreOnRemote(String remoteDumpPath) throws Exception {
        String fileName = Paths.get(remoteDumpPath).getFileName().toString();
        String containerPath = "/tmp/" + fileName;
        String dockerCmdPrefix = useSudoDocker ? "sudo docker" : "docker";

        // 1. docker cp
        String dockerCpCmd = String.format("%s cp %s %s:%s",
                dockerCmdPrefix, remoteDumpPath, containerId, containerPath);
        System.out.println("[BackupService] " + dockerCpCmd);
        sshUtils.execRemoteCommand(dockerCpCmd, 120_000);

        // 2. Завершаем подключения к базе (если нужно)
        String terminateConn = String.format(
                "SELECT pg_terminate_backend(pid) FROM pg_stat_activity WHERE datname = '%s' AND pid <> pg_backend_pid();",
                remoteDbName
        );
        String dockerExecTerminate = String.format("%s exec -e PGPASSWORD=%s %s psql -U %s -d postgres -c \"%s\"",
                dockerCmdPrefix,
                quoteShell(remoteDbPassword),
                containerId,
                remoteDbUser,
                terminateConn.replace("\"", "\\\"")
        );
        System.out.println("[BackupService] Завершаем подключения: " + dockerExecTerminate);
        try {
            String outTerm = sshUtils.execRemoteCommand(dockerExecTerminate, 120_000);
            System.out.println("[BackupService] Terminate output: " + outTerm);
        } catch (Exception e) {
            // Логируем, но не останавливаем, возможно нет активных подключений
            System.err.println("[BackupService] Ошибка при завершении подключений: " + e.getMessage());
        }

        // 3. DROP DATABASE IF EXISTS
        String dropCmdInner = String.format("DROP DATABASE IF EXISTS %s;", remoteDbName);
        String dockerExecDrop = String.format("%s exec -e PGPASSWORD=%s %s psql -U %s -d postgres -c \"%s\"",
                dockerCmdPrefix,
                quoteShell(remoteDbPassword),
                containerId,
                remoteDbUser,
                dropCmdInner
        );
        System.out.println("[BackupService] Выполняем DROP: " + dockerExecDrop);
        String outDrop = sshUtils.execRemoteCommand(dockerExecDrop, 300_000);
        System.out.println("[BackupService] DROP output: " + outDrop);

        // 4. CREATE DATABASE
        String createCmdInner = String.format("CREATE DATABASE %s;", remoteDbName);
        String dockerExecCreate = String.format("%s exec -e PGPASSWORD=%s %s psql -U %s -d postgres -c \"%s\"",
                dockerCmdPrefix,
                quoteShell(remoteDbPassword),
                containerId,
                remoteDbUser,
                createCmdInner
        );
        System.out.println("[BackupService] Выполняем CREATE: " + dockerExecCreate);
        String outCreate = sshUtils.execRemoteCommand(dockerExecCreate, 300_000);
        System.out.println("[BackupService] CREATE output: " + outCreate);

        // 5. pg_restore внутри контейнера с пропуском восстановления владельцев
        String dockerExecRestore = String.format(
                "%s exec -e PGPASSWORD=%s %s pg_restore -U %s -d %s -v -O --no-privileges %s",
                dockerCmdPrefix,
                quoteShell(remoteDbPassword),
                containerId,
                remoteDbUser,
                remoteDbName,
                containerPath
        );
        System.out.println("[BackupService] Выполняем pg_restore: " + dockerExecRestore);
        String outRestore = sshUtils.execRemoteCommand(dockerExecRestore, 300_000);
        System.out.println("[BackupService] pg_restore output: " + outRestore);

        // 6. Удаляем внутри контейнера
        String rmCmd = String.format("%s exec %s rm -f %s", dockerCmdPrefix, containerId, containerPath);
        System.out.println("[BackupService] Удаляем дамп внутри контейнера: " + rmCmd);
        try {
            String outRm = sshUtils.execRemoteCommand(rmCmd, 30_000);
            System.out.println("[BackupService] rm output: " + outRm);
        } catch (Exception e) {
            System.err.println("[BackupService] Не удалось удалить дамп внутри контейнера: " + e.getMessage());
        }
    }

    /**
     * Экранирует значение для включения в команду оболочки.
     */
    private String quoteShell(String s) {
        if (s == null) return "";
        return "'" + s.replace("'", "'\"'\"'") + "'";
    }


    private void cleanupLocalDump(String dumpFileName) {
        try {
            Path path = Paths.get(localDumpDir, dumpFileName);
            Files.deleteIfExists(path);
            System.out.println("[BackupService] Удалён локальный дамп: " + path);
        } catch (IOException e) {
            System.err.println("[BackupService] Не удалось удалить локальный дамп: " + e.getMessage());
        }
    }

    private void cleanupRemoteDump(String remoteDumpPath) {
        try {
            String cmd = "rm -f " + remoteDumpPath;
            sshUtils.execRemoteCommand(cmd, 10_000);
            System.out.println("[BackupService] Удалён remote дамп: " + remoteDumpPath);
        } catch (Exception e) {
            System.err.println("[BackupService] Не удалось удалить remote дамп: " + e.getMessage());
        }
    }

    /**
     * Проверяет доступность локальной БД.
     * Возвращает true, если удалось подключиться, иначе false.
     */
    private boolean isLocalDbAvailable() {
        String url = String.format("jdbc:postgresql://%s:%d/%s", primaryHost, primaryPort, primaryDbName);
        try (Connection conn = DriverManager.getConnection(url, primaryUser, primaryPassword)) {
            return conn.isValid(2);
        } catch (SQLException e) {
            System.err.println("[BackupService] Локальная БД недоступна: " + e.getMessage());
            return false;
        }
    }
}