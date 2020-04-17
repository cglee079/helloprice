package com.podo.helloprice.notifier.mq.consumer;

import com.podo.helloprice.notifier.infra.email.GmailClient;
import com.podo.helloprice.notifier.mq.message.EmailNotifyMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@RequiredArgsConstructor
@Slf4j
@Component
public class EmailNotifyMessageConsumer implements Consumer<EmailNotifyMessage> {

    private final GmailClient gmailClient;

    @Override
    public void accept(EmailNotifyMessage emailNotifyMessage) {
        log.debug("MQ :: CONSUME :: {}", emailNotifyMessage);

        final String email = emailNotifyMessage.getEmail();
        final String username = emailNotifyMessage.getUsername();
        final String title = emailNotifyMessage.getTitle();
        final String contents = emailNotifyMessage.getContents();

        gmailClient.sendEmail(username, email, title, contents);
    }
}
