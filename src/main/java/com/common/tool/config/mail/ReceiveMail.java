package com.common.tool.config.mail;

import javax.mail.*;
import javax.mail.internet.ContentType;
import javax.mail.internet.MimeUtility;
import javax.mail.internet.ParameterList;
import javax.mail.internet.ParseException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

/*获取邮件 还需优化*/
public class ReceiveMail {

    public static void main(String[] args) {
        String username = ""; // 邮箱账号
        String password = ""; // 邮箱密码
        String protocol = ""; // 接收协议
        String port = ""; // 端口
        String host = protocol + "." + ""; // 邮件服务器主机名
        // 设置连接属性
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", protocol);
        props.setProperty("mail.imap.host", host);
        props.setProperty("mail.imap.port", port);
        props.setProperty("mail.imap.ssl.enable", "true");
        try {
            // 连接到邮件服务器
            Session session = Session.getDefaultInstance(props);
            Store store = session.getStore(protocol);
            store.connect(host, username, password);
            // 打开收件箱
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);
            // 获取最近100封邮件
            int messageCount = inbox.getMessageCount();
            int startIndex = Math.max(1, messageCount - 100 + 1); // 最近的100封邮件的起始索引
            Message[] messages = inbox.getMessages(startIndex, messageCount);
            // 将邮件按照时间戳排序
            Arrays.sort(messages, new Comparator<Message>() {
                @Override
                public int compare(Message message1, Message message2) {
                    try {
                        Date date1 = message1.getSentDate();
                        Date date2 = message2.getSentDate();
                        if (date1 != null && date2 != null) {
                            return date2.compareTo(date1);
                        }
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                    return 0;
                }
            });
            for (Message message : messages) {
                System.out.println("---------------------------分割线----------------------------");
                String title = message.getSubject();
                //邮件标题
                System.out.println("标题：" + title);
                //邮件发送时间
                String send_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(message.getSentDate());
                System.out.println("发送时间：" + send_time);
                Object content = message.getContent();
                //定义保存邮件附件路径
                String dir = "D:/tmp/" + UUID.randomUUID().toString().replace("-", "") + "/";
                //邮件正文
                processMultipartRelated((Multipart) content, dir);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void processMultipartRelated(Multipart multipart, String dir) throws MessagingException, IOException {
        for (int i = 0; i < multipart.getCount(); i++) {
            BodyPart bodyPart = multipart.getBodyPart(i);
            String contentType = bodyPart.getContentType();
            if (bodyPart.getDisposition() != null && bodyPart.getDisposition().equalsIgnoreCase(Part.ATTACHMENT)) {
                String originalFilename = decodeName(contentType);
                System.out.println("邮件附件：" + originalFilename);
                InputStream inputStream = bodyPart.getInputStream();
                // 保存附件
                File file = new File(dir);
                if (!file.exists()) {
                    file.mkdirs();
                }
                Path path = Paths.get(dir + originalFilename);
                Files.copy(inputStream, path);
                System.out.println("附件保存成功：" + dir + originalFilename);
                continue;
            }
            // 检查Content-Type
            if (contentType.startsWith("text/plain") || contentType.startsWith("text/html")) {
                // 获取正文内容
                Object content = bodyPart.getContent();
                if (content instanceof String) {
                    System.out.println("邮件正文：" + content);
                }
            } else if (contentType.startsWith("multipart/related")) {
                // 如果仍然是multipart/related类型，则递归处理
                processMultipartRelated((Multipart) bodyPart.getContent(), dir);
            }
        }
    }

    /*
        这个代码尝试解析contentType，然后从ParameterList中获取name参数，并最终使用MimeUtility.decodeText进行解码。请注意，这里使用了ContentType类，它可以帮助你更方便地处理Content-Type头部。
    */
    private static String decodeName(String contentType) throws ParseException, UnsupportedEncodingException {
        String encodedName = "";
        ContentType ct = new ContentType(contentType);
        ParameterList parameterList = ct.getParameterList();
        Enumeration names = parameterList.getNames();
        while (names.hasMoreElements()) {
            encodedName += parameterList.get((String) names.nextElement());
        }
        return MimeUtility.decodeText(encodedName);
    }

}
