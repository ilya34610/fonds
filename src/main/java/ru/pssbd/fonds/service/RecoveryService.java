package ru.pssbd.fonds.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.pssbd.fonds.config.SSHUtils;

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
public class RecoveryService {

    @Value("${app.ssh.remote.host}") private String sshHost;
    @Value("${app.ssh.remote.port}") private int sshPort;
    @Value("${app.ssh.remote.user}") private String sshUser;
    @Value("${app.ssh.remote.password:}") private String sshPassword;
    @Value("${app.ssh.remote.private-key-path:}") private String sshKeyPath;

    @Value("${app.db.remote.container-id}") private String containerId;
    @Value("${app.db.remote.use-sudo-docker:false}") private boolean useSudoDocker;

    @Value("${app.backup.local-dump-dir}") private String localDumpDir;
    @Value("${app.backup.remote-dump-dir}") private String remoteDumpDir; // временная папка на remote-host

    @Value("${app.db.remote.user}") private String standbyUser;
    @Value("${app.db.remote.password}") private String standbyPassword;
    @Value("${app.db.remote.name}") private String standbyDbName;

    @Value("${app.db.primary.user}") private String primaryUser;
    @Value("${app.db.primary.password}") private String primaryPassword;
    @Value("${app.db.primary.host}") private String primaryHost;
    @Value("${app.db.primary.port}") private int primaryPort;
    @Value("${app.db.primary.name}") private String primaryDbName;
    @Value("${app.backup.pg-psql-path}")
    private String psqlPath;

    @Value("${app.backup.pg-restore-path}")
    private String pgRestorePath;


    private SSHUtils sshUtils;

    @PostConstruct
    public void init() {
        sshUtils = new SSHUtils(sshUser, sshHost, sshPort, sshKeyPath, sshPassword);
        // Убедиться, что localDumpDir существует
        try {
            Path local = Paths.get(localDumpDir);
            if (!Files.exists(local)) Files.createDirectories(local);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Выполняет синхронизацию standby -> primary при восстановлении primary.
     * Предполагается, что приложение уже приостановило записи или находится в maintenance.
     */
    public void syncStandbyToPrimary() throws Exception {
        // 1. Сгенерировать имя дампа: e.g. standby_yyyyMMdd_HHmmss.dump
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = "standby_" + timestamp + ".dump";
        String remoteDumpPath = remoteDumpDir.endsWith("/") ? remoteDumpDir + fileName : remoteDumpDir + "/" + fileName;

        // 2. На remote: внутри контейнера или на хосте делать pg_dump
        // Предположим, контейнер запущен, и pg_dump доступен внутри.
        String dockerCmdPrefix = useSudoDocker ? "sudo docker" : "docker";
        // Пусть внутри контейнера путь /tmp/<filename>
        String containerDumpPath = "/tmp/" + fileName;

        // Команда: docker exec <container> pg_dump -U standbyUser -F c -b -v -f /tmp/fileName standbyDbName
        String pgDumpCmdInner = String.format(
                "PGPASSWORD=%s pg_dump -U %s -F c -b -v -f %s %s",
                quoteShell(standbyPassword),
                standbyUser,
                containerDumpPath,
                standbyDbName
        );
        String dockerExecDump = String.format("%s exec -e PGPASSWORD=%s %s sh -c \"%s\"",
                dockerCmdPrefix,
                quoteShell(standbyPassword),
                containerId,
                pgDumpCmdInner.replace("\"", "\\\"")
        );
        System.out.println("[RecoveryService] Выполняем на remote: " + dockerExecDump);
        sshUtils.execRemoteCommand(dockerExecDump, 600_000);

        // 3. Копируем дамп из контейнера на хост (если не примонтировано):
        //   docker cp <container>:/tmp/fileName <remoteDumpPath>
        String dockerCpCmd = String.format("%s cp %s:%s %s",
                dockerCmdPrefix, containerId, containerDumpPath, remoteDumpPath);
        System.out.println("[RecoveryService] docker cp: " + dockerCpCmd);
        sshUtils.execRemoteCommand(dockerCpCmd, 120_000);

        // 4. Опционально удалить внутри контейнера
        String rmContainerCmd = String.format("%s exec %s rm -f %s", dockerCmdPrefix, containerId, containerDumpPath);
        sshUtils.execRemoteCommand(rmContainerCmd, 30_000);

        // 5. SCP: копируем с remote-host на локальную Windows машину
        Path localPath = Paths.get(localDumpDir, fileName);
        // Для scp из remote на локал, можно использовать обратный SSH: но SSHUtils умеет scpToRemote только в одну сторону.
        // Решение: выполнить на local команду scp: requires SSH доступ из local->remote.
        // Предполагаем, SSHUtils выполняется из local, так он может scp local->remote.
        // Для обратной стороны: используем JSch SFTP для получения файла:
        downloadFileViaSftp(remoteDumpPath, localPath.toString());

        // 6. На локальной машине: выполнить восстановление базы primary
        // 6.1 Завершаем подключения к локальной базе, если есть
        terminateLocalConnections(primaryDbName);
        // 6.2 DROP и CREATE локальной базы
        recreateLocalDatabase(primaryDbName);
        // 6.3 pg_restore локально
        restoreLocalDump(localPath.toString());

        // 7. Опционально удалить remote и локальный дамп
        sshUtils.execRemoteCommand("rm -f " + remoteDumpPath, 10_000);
        Files.deleteIfExists(localPath);
    }

    private void downloadFileViaSftp(String remoteFilePath, String localFilePath) throws Exception {
        // Используем JSch напрямую для SFTP загрузки
        com.jcraft.jsch.JSch jsch = new com.jcraft.jsch.JSch();
        com.jcraft.jsch.Session session = jsch.getSession(sshUser, sshHost, sshPort);
        if (sshKeyPath != null && !sshKeyPath.isBlank()) {
            jsch.addIdentity(sshKeyPath);
        } else if (sshPassword != null) {
            session.setPassword(sshPassword);
        }
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect(10_000);
        com.jcraft.jsch.ChannelSftp sftp = (com.jcraft.jsch.ChannelSftp) session.openChannel("sftp");
        sftp.connect(5_000);

        // Ensure local dir exists
        Path lp = Paths.get(localFilePath).getParent();
        if (lp != null && !Files.exists(lp)) Files.createDirectories(lp);

        sftp.get(remoteFilePath, localFilePath);
        System.out.println("[RecoveryService] Скачан дамп standby: " + localFilePath);
        sftp.disconnect();
        session.disconnect();
    }

    private void terminateLocalConnections(String dbName) {
        String url = String.format("jdbc:postgresql://%s:%d/%s", primaryHost, primaryPort, "postgres");
        try (Connection conn = DriverManager.getConnection(url, primaryUser, primaryPassword);
             java.sql.Statement stmt = conn.createStatement()) {
            String sql = String.format(
                    "SELECT pg_terminate_backend(pid) FROM pg_stat_activity WHERE datname='%s' AND pid<>pg_backend_pid();",
                    dbName);
            stmt.execute(sql);
            System.out.println("[RecoveryService] Завершили локальные подключения к " + dbName);
        } catch (SQLException e) {
            System.err.println("[RecoveryService] Ошибка terminate connections: " + e.getMessage());
        }
    }

    private void recreateLocalDatabase(String dbName) throws IOException, InterruptedException {
        // Используем полный путь к psql.exe
        String psql = psqlPath; // например "C:\\Program Files\\PostgreSQL\\14\\bin\\psql.exe"

        // DROP
        ProcessBuilder pbDrop = new ProcessBuilder(
                psql,
                "-h", primaryHost,
                "-p", String.valueOf(primaryPort),
                "-U", primaryUser,
                "-d", "postgres",
                "-c", String.format("DROP DATABASE IF EXISTS %s;", dbName)
        );
        pbDrop.environment().put("PGPASSWORD", primaryPassword);
        pbDrop.redirectErrorStream(true);
        Process pDrop = pbDrop.start();
        try (BufferedReader r = new BufferedReader(new InputStreamReader(pDrop.getInputStream()))) {
            String line;
            while ((line = r.readLine()) != null) System.out.println("[psql DROP] " + line);
        }
        int codeDrop = pDrop.waitFor();
        if (codeDrop != 0) throw new IOException("Ошибка DROP локальной БД, код=" + codeDrop);

        // CREATE
        ProcessBuilder pbCreate = new ProcessBuilder(
                psql,
                "-h", primaryHost,
                "-p", String.valueOf(primaryPort),
                "-U", primaryUser,
                "-d", "postgres",
                "-c", String.format("CREATE DATABASE %s;", dbName)
        );
        pbCreate.environment().put("PGPASSWORD", primaryPassword);
        pbCreate.redirectErrorStream(true);
        Process pCreate = pbCreate.start();
        try (BufferedReader r = new BufferedReader(new InputStreamReader(pCreate.getInputStream()))) {
            String line;
            while ((line = r.readLine()) != null) System.out.println("[psql CREATE] " + line);
        }
        int codeCreate = pCreate.waitFor();
        if (codeCreate != 0) throw new IOException("Ошибка CREATE локальной БД, код=" + codeCreate);

        System.out.println("[RecoveryService] Локальная БД пересоздана: " + dbName);
    }

    private void restoreLocalDump(String localDumpPath) throws IOException, InterruptedException {
        // pg_restore -h localhost -p port -U user -d dbName -v --no-owner --no-privileges localDumpPath
        String pgRestore = pgRestorePath;
        ProcessBuilder pb = new ProcessBuilder(
                pgRestore,
                "-h", primaryHost,
                "-p", String.valueOf(primaryPort),
                "-U", primaryUser,
                "-d", primaryDbName,
                "-v",
                "-O",
                "--no-privileges",
                localDumpPath
        );
        pb.environment().put("PGPASSWORD", primaryPassword);
        pb.redirectErrorStream(true);
        Process p = pb.start();
        try (BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            String line;
            while ((line = r.readLine()) != null) System.out.println("[pg_restore local] " + line);
        }
        int code = p.waitFor();
        if (code != 0) throw new IOException("Ошибка pg_restore локальной БД, код=" + code);
        System.out.println("[RecoveryService] Локальная БД восстановлена из standby дампа");
    }

    private String quoteShell(String s) {
        if (s == null) return "";
        return "'" + s.replace("'", "'\"'\"'") + "'";
    }
}

