package com.common.tool.config.mail;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

/*发送邮件*/
public class SendMail {

    public static void main(String[] args) {
        String username = ""; // 邮箱账号
        String password = ""; // 邮箱密码
        String protocol = ""; // 协议
        String host = protocol + "." + ""; // 邮件服务器主机名
        Properties props = new Properties();
        props.setProperty("mail.smtp.host", host);
        props.setProperty("mail.smtp.auth", "true");
        try {
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });
            MimeMessage message = new MimeMessage(session);
            String from = "zwzxxz@js.chinamobile.com";//发件人邮件
            message.setFrom(new InternetAddress(from));

            //设置收件人
            String to_address = "";//收件人邮件
            InternetAddress[] to = InternetAddress.parse(to_address);
            message.addRecipients(Message.RecipientType.TO, to);//收件人邮件
            //设置抄送
            String cc_address = "";//抄送邮箱
            InternetAddress[] cc = InternetAddress.parse(cc_address);
            message.addRecipients(Message.RecipientType.CC, cc);
            //设置邮件主题
            message.setSubject("");

            Multipart multipart = new MimeMultipart();

            //添加正文文本部分
            MimeBodyPart text = new MimeBodyPart();
            //文本内容
            String mail_content = "";
            text.setContent(mail_content, "text/html;charset=utf-8");//识别html文本格式
            multipart.addBodyPart(text);

            //添加正文图片部分
            MimeBodyPart photo = new MimeBodyPart();
            FileDataSource photoDataSource = new FileDataSource("");//设置图片地址
            photo.setDataHandler(new DataHandler(photoDataSource));
            photo.setHeader("Content-ID", "<image0>");
            multipart.addBodyPart(photo);

            //设置邮件附件
            MimeBodyPart file = new MimeBodyPart();
            String fileName = "";//附件名称
            String filePath = "";//附件路径

            //若附件名称带有中文直接发出去附件名称会乱码
            //方式1解决乱码
            /*
                fileName = URLEncoder.encode(fileName, "UTF-8");
                //或者
                fileName = MimeUtility.encodeText(fileName, "UTF-8", "B");
                file.setFileName(fileName);
                file.attachFile(filePath);
                multipart.addBodyPart(file);
            */
            //方式2解决乱码
            fileName = MimeUtility.encodeText(fileName, "UTF-8", "B");
            file.setFileName(fileName);
            FileDataSource fileDataSource = new FileDataSource(filePath);
            file.setDataHandler(new DataHandler(fileDataSource));
            multipart.addBodyPart(file);

            //填充发送
            message.setContent(multipart);
            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
