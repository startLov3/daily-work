package com.common.tool.config.jsch;

import com.jcraft.jsch.*;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

/*Java通过JSch库处理SFTP连接和文件传输，下载远程服务器中的文件。*/
public class JSchSFTPFileTransfer {

    public static void main(String[] args) {
        String host = "";//服务器地址
        String user = "";//用户
        String password = "";//密码
        int port = 6025;//端口
        String remoteFilePath = "";//拉取文件的路径
        String localFilePath = "";//下载到本地路径
        JSch jsch = new JSch();
        // 初始化对象
        Session session = null;
        ChannelSftp sftpChannel = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            // 创建会话
            session = jsch.getSession(user, host, port);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(password);
            session.connect();
            // 打开 SFTP 通道
            sftpChannel = (ChannelSftp) session.openChannel("sftp");
            sftpChannel.connect();
            //创建本地路径
            int lastSlashIndex = remoteFilePath.lastIndexOf("/");
            String path = remoteFilePath.substring(0, lastSlashIndex);
            File file = new File(path);
            if (!file.exists()) {
                try {
                    file.mkdirs();
                } catch (Exception e) {
                    System.out.println("=====jSchSFTPFileTransfer创建文件夹失败！====");
                }
            }
            // 下载文件
            inputStream = sftpChannel.get(remoteFilePath);
            // 上传文件到本地
            outputStream = new java.io.FileOutputStream(localFilePath);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            System.out.println("=====jSchSFTPFileTransfer文件下载成功！===="+localFilePath);
        } catch (JSchException | SftpException | java.io.IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭流、通道和会话
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
            }
            if (sftpChannel != null && sftpChannel.isConnected()) {
                sftpChannel.disconnect();
            }
            if (session != null && session.isConnected()) {
                session.disconnect();
            }
        }
    }
}