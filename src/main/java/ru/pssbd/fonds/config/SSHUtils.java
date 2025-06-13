package ru.pssbd.fonds.config;

import com.jcraft.jsch.*;
import java.io.*;
import java.util.Properties;

public class SSHUtils {
    private String sshUser;
    private String sshHost;
    private int sshPort;
    private String privateKeyPath;
    private String sshPassword;

    public SSHUtils(String sshUser, String sshHost, int sshPort, String privateKeyPath, String sshPassword) {
        this.sshUser = sshUser;
        this.sshHost = sshHost;
        this.sshPort = sshPort;
        this.privateKeyPath = privateKeyPath;
        this.sshPassword = sshPassword;
    }

    private Session createSession() throws JSchException {
        JSch jsch = new JSch();
        if (privateKeyPath != null && !privateKeyPath.isBlank()) {
            jsch.addIdentity(privateKeyPath);
        }
        Session session = jsch.getSession(sshUser, sshHost, sshPort);
        if ((privateKeyPath == null || privateKeyPath.isBlank()) && sshPassword != null) {
            session.setPassword(sshPassword);
        }
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect(10_000);
        return session;
    }

    /**
     * Копирует локальный файл localFilePath на удалённый remoteTargetPath.
     */
    public void scpToRemote(String localFilePath, String remoteTargetPath) throws JSchException, SftpException {
        Session session = createSession();
        ChannelSftp sftp = (ChannelSftp) session.openChannel("sftp");
        sftp.connect(5_000);
        try {
            sftp.put(localFilePath, remoteTargetPath);
            System.out.println("[SSHUtils] Передал " + localFilePath + " -> " + sshHost + ":" + remoteTargetPath);
        } finally {
            sftp.disconnect();
            session.disconnect();
        }
    }

    /**
     * Выполняет удалённую команду через SSH и возвращает вывод stdout.
     */
    public String execRemoteCommand(String command, int timeoutMs) throws JSchException, IOException {
        Session session = createSession();
        ChannelExec channel = (ChannelExec) session.openChannel("exec");
        channel.setCommand(command);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
        channel.setOutputStream(outputStream);
        channel.setErrStream(errorStream);
        channel.connect();

        long start = System.currentTimeMillis();
        while (!channel.isClosed()) {
            if (System.currentTimeMillis() - start > timeoutMs) {
                channel.disconnect();
                throw new IOException("Timeout executing remote command");
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        int exitStatus = channel.getExitStatus();
        String out = outputStream.toString();
        String err = errorStream.toString();
        channel.disconnect();
        session.disconnect();
        if (exitStatus != 0) {
            throw new IOException("Remote command failed, exit=" + exitStatus + ", stderr=" + err);
        }
        return out;
    }
}