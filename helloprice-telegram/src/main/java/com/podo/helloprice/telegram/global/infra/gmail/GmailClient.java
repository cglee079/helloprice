package com.podo.helloprice.telegram.global.infra.gmail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;


@Slf4j
@RequiredArgsConstructor
@Component
public class GmailClient {

    @Value("${app.name}")
    private String appName;

    @Value("${infra.gmail.send.timeout}")
    private Integer timeout;

    @Value("${infra.gmail.admin.email}")
    private String adminEmail;

    private final Authenticator gmailAuth;

    public void sendAdmin(String title, String contents) {
        sendEmail(appName, adminEmail, title, contents);
    }

    public void sendEmail(String username, String userEmail, String title, String contents) {
        log.info("'{}({})'로 메일을 발송합니다, 메일제목 : {}", userEmail, username, title);

        try {
            final Properties mailProperties = this.getMailProperties();
            final Session messageSession = Session.getInstance(mailProperties, gmailAuth);
            final Transport transport = messageSession.getTransport("smtp");
            transport.connect();

            final MimeMessage message = createMessage(username, userEmail, title, contents, messageSession);
            transport.sendMessage(message, message.getAllRecipients());

        } catch (Exception e) {
            log.error("메일 전송에 문제가 발생하였습니다 {}", e.getMessage());
        }

    }

    private MimeMessage createMessage(String username, String userEmail, String title, String contents, Session msgSession) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = new MimeMessage(msgSession);

        message.setFrom(new InternetAddress(adminEmail, appName));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(userEmail, username));
        message.setSubject(title);
        message.setContent(contents, "text/html; charset=utf-8");

        return message;
    }

    private Properties getMailProperties() {
        final Properties properties = new Properties();

        properties.put("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.put("mail.debug", "false");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.connectiontimeout", timeout);
        properties.put("mail.smtp.timeout", timeout);
        properties.put("mail.smtp.auth", true);

        return properties;
    }

}