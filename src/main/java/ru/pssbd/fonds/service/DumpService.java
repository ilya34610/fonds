package ru.pssbd.fonds.service;

import lombok.Getter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.UUID;

@Service
public class DumpService {

    // === Внедряем параметры из application.properties ===
    @Value("${spring.datasource.primary.username}")
    private String localDbUser;

    @Value("${spring.datasource.primary.password}")
    private String localDbPassword;

    @Value("${spring.datasource.primary.url}")
    private String localDbUrl; // вида jdbc:postgresql://localhost:5432/fonds

    @Value("${app.local.dump.dir}")
    private String localDumpDir;

    @Value("${app.remote.ssh.host}")
    private String remoteHost;

    @Value("${app.remote.ssh.port}")
    private int remotePort;

    @Value("${app.remote.ssh.user}")
    private String remoteSshUser;

    @Value("${app.remote.ssh.password}")
    private String remoteSshPassword;

    @Value("${app.remote.dump.dir}")
    private String remoteDumpDir;

    @Value("${app.remote.db.name}")
    private String remoteDbName;

    @Value("${app.remote.db.user}")
    private String remoteDbUser;

    @Value("${app.remote.db.password}")
    private String remoteDbPassword;

//    @Scheduled(cron = "0 0 2 * * *") // каждый день в 2:00
//    public void scheduledSync() throws Exception {
//        backupAndRestore();
//    }

    /**
     * Основной метод: создаёт дамп локальной базы, заливает его на удалённый сервер и выполняет восстановление там.
     */
    public void backupAndRestore() throws Exception {
        // 1. Парсим localDbUrl, чтобы достать host, порт и название БД
        DbInfo localInfo = parseJdbcUrl(localDbUrl);

        // 2. Генерируем уникальное имя файла дампа, например: fonds_dump_20250601_UUID.sql
        String filename = generateDumpFileName(localInfo.getDbName());
        String localFilePath = Paths.get(localDumpDir, filename).toString();

        // 3. Создаём дамп локальной БД
        createLocalDump(localInfo.getHost(), localInfo.getPort(), localInfo.getDbName(),
                localDbUser, localDbPassword, localFilePath);

        // 4. По SSH/SFTP пересылаем файл на удалённый сервер
        String remoteFilePath = remoteDumpDir + "/" + filename;
        uploadFileViaSftp(localFilePath, remoteFilePath);

        // 5. На удалённом сервере выполняем восстановление через psql
        restoreRemoteDump(remoteFilePath);

        // 6. (Опционально) Можно удалить локальный дамп, если он больше не нужен
        new File(localFilePath).delete();
    }

    /**
     * Шаг 1: Парсинг JDBC URL вида jdbc:postgresql://host:port/dbName
     */
    private DbInfo parseJdbcUrl(String jdbcUrl) {
        // Пример: jdbc:postgresql://localhost:5432/fonds
        String withoutPrefix = jdbcUrl.replace("jdbc:postgresql://", "");
        String[] hostPortAndDb = withoutPrefix.split("/", 2);
        String hostPort = hostPortAndDb[0];     // localhost:5432
        String dbName = hostPortAndDb[1];       // fonds
        String[] hp = hostPort.split(":", 2);
        String host = hp[0];
        int port = Integer.parseInt(hp[1]);
        return new DbInfo(host, port, dbName);
    }

    /**
     * Шаг 2: Генерация имени дамп-файла
     */
    private String generateDumpFileName(String dbName) {
        // Например: fonds_dump_<yyyyMMdd>_<UUID>.sql
        String datePart = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.BASIC_ISO_DATE);
        String uuidPart = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        return String.format("%s_dump_%s_%s.sql", dbName, datePart, uuidPart);
    }

    /**
     * Шаг 3: Создаём дамп локальной БД с помощью pg_dump
     */
    private void createLocalDump(String host, int port, String dbName,
                                 String user, String password,
                                 String outputFile) throws IOException, InterruptedException {

        // Формируем команду:
        // pg_dump -h <host> -p <port> -U <user> -F p -f <outputFile> <dbName>
        ProcessBuilder pb = new ProcessBuilder(
                "pg_dump",
                "-h", host,
                "-p", String.valueOf(port),
                "-U", user,
                "-F", "p",             // plain text format
                "-f", outputFile,
                dbName
        );

        // Передаём пароль через переменную окружения PGPASSWORD
        pb.environment().put("PGPASSWORD", password);

        // Перенаправляем stdout/stderr в консоль (для отладки)
        pb.redirectErrorStream(true);
        pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);

        Process process = pb.start();
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("pg_dump вернул код " + exitCode);
        }
        System.out.println("Дамп локальной БД успешно создан: " + outputFile);
    }

    /**
     * Шаг 4: Передача файла на удалённый сервер по SFTP
     */
    private void uploadFileViaSftp(String localFilePath, String remoteFilePath) throws Exception {
        JSch jsch = new JSch();
        Session session = jsch.getSession(remoteSshUser, remoteHost, remotePort);
        session.setPassword(remoteSshPassword);

        // Отключим проверку host‐ключа (не рекомендуется для продакшена, но для демонстрации ок)
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);

        session.connect(10_000); // таймаут 10 сек
        System.out.println("SSH‐сессия установлена: " + remoteHost);

        ChannelSftp sftp = (ChannelSftp) session.openChannel("sftp");
        sftp.connect(5_000);
        System.out.println("SFTP‐канал открыт.");

        // Создаём папку на удалённом сервере, если её нет
        String remoteDir = remoteFilePath.substring(0, remoteFilePath.lastIndexOf("/"));
        try {
            sftp.cd(remoteDir);
        } catch (Exception ex) {
            // Если папки нет — создаём вложенные
            String[] dirs = remoteDir.split("/");
            String pathSoFar = "";
            for (String d : dirs) {
                if (d.isEmpty()) continue;
                pathSoFar += "/" + d;
                try {
                    sftp.cd(pathSoFar);
                } catch (Exception e) {
                    sftp.mkdir(pathSoFar);
                    sftp.cd(pathSoFar);
                }
            }
        }

        // Загружаем файл: локальный путь → удалённый путь
        sftp.put(localFilePath, remoteFilePath);
        System.out.println("Файл загружен на удалённый сервер: " + remoteFilePath);

        sftp.disconnect();
        session.disconnect();
    }

    /**
     * Шаг 5: На удалённом сервере выполняем восстановление через psql
     */
    private void restoreRemoteDump(String remoteFilePath) throws Exception {
        JSch jsch = new JSch();
        Session session = jsch.getSession(remoteSshUser, remoteHost, remotePort);
        session.setPassword(remoteSshPassword);
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);

        session.connect(10_000);
        System.out.println("SSH‐сессия для восстановления установлена.");

        // Команда для восстановления:
        // export PGPASSWORD=<remoteDbPassword> && psql -U <remoteDbUser> -d <remoteDbName> -f <remoteFilePath>
        String restoreCommand = String.format(
                "export PGPASSWORD=%s && psql -U %s -d %s -f %s",
                escapeShellArg(remoteDbPassword),
                escapeShellArg(remoteDbUser),
                escapeShellArg(remoteDbName),
                escapeShellArg(remoteFilePath)
        );

        ChannelExec exec = (ChannelExec) session.openChannel("exec");
        exec.setCommand(restoreCommand);

        // Получаем поток вывода и ошибок
        InputStream in = exec.getInputStream();
        InputStream err = exec.getErrStream();

        exec.connect();

        // Считываем stdout
        byte[] tmp = new byte[1024];
        while (true) {
            while (in.available() > 0) {
                int i = in.read(tmp, 0, 1024);
                if (i < 0) break;
                System.out.print(new String(tmp, 0, i));
            }
            while (err.available() > 0) {
                int i = err.read(tmp, 0, 1024);
                if (i < 0) break;
                System.err.print(new String(tmp, 0, i));
            }
            if (exec.isClosed()) {
                if (in.available() > 0 || err.available() > 0) continue;
                int exitStatus = exec.getExitStatus();
                if (exitStatus != 0) {
                    throw new RuntimeException("Ошибка psql на удалённом сервере, код: " + exitStatus);
                }
                break;
            }
            Thread.sleep(100);
        }

        exec.disconnect();
        session.disconnect();

        System.out.println("Восстановление на удалённом сервере прошло успешно.");
    }

    /**
     * Безопасная экранизация аргументов для shell (простая версия)
     */
    private String escapeShellArg(String arg) {
        return arg.replace("'", "'\"'\"'");
    }

    /**
     * Вспомогательный класс для хранения разобранных частей JDBC URL
     */
    @Getter
    private static class DbInfo {
        private final String host;
        private final int port;
        private final String dbName;

        public DbInfo(String host, int port, String dbName) {
            this.host = host;
            this.port = port;
            this.dbName = dbName;
        }

    }


}
