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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Slf4j
@RequiredArgsConstructor
@Component
public class GmailClient {

    @Value("${app.name}")
    private final String appName;

    @Value("${infra.gmail.send.timeout}")
    private final Integer sendTimeout;

    @Value("${infra.gmail.admin.email}")
    private final String adminEmail;

    private final Authenticator gmailAuth;

    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    public void sendEmailToAdmin(String title, String contents) {
        sendEmail(appName, adminEmail, title, contents);
    }

    public void sendEmail(String username, String userEmail, String title, String contents) {

        executorService.execute(() -> {

            log.debug("EMAIL :: '{}({})'로 메일을 발송합니다, 메일제목 : {}", userEmail, username, title);

            try {
                final Properties mailProperties = getMailProperties();
                final Session messageSession = Session.getInstance(mailProperties, gmailAuth);
                final Transport transport = messageSession.getTransport("smtp");
                transport.connect();

                final MimeMessage message = createMessage(username, userEmail, title, contents, messageSession);
                transport.sendMessage(message, message.getAllRecipients());

            } catch (Exception e) {
                log.error("메일 전송에 문제가 발생하였습니다 {}", e.getMessage());
            }

        });

    }

    private MimeMessage createMessage(String username, String userEmail, String title, String contents, Session msgSession) throws MessagingException, UnsupportedEncodingException {
        final MimeMessage message = new MimeMessage(msgSession);

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
        properties.put("mail.smtp.connectiontimeout", sendTimeout);
        properties.put("mail.smtp.timeout", sendTimeout);
        properties.put("mail.smtp.auth", true);

        return properties;
    }

}