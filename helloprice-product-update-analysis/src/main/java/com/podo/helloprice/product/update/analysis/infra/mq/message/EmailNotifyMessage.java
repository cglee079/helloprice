package com.podo.helloprice.product.update.analysis.infra.mq.message;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class EmailNotifyMessage {
    private String email;
    private String username;
    private String title;
    private String contents;

    public static EmailNotifyMessage create(String email, String username, String title, String contents) {
        final EmailNotifyMessage emailNotifyMessage = new EmailNotifyMessage();

        emailNotifyMessage.email = email;
        emailNotifyMessage.username = username;
        emailNotifyMessage.title = title;
        emailNotifyMessage.contents = contents;

        return emailNotifyMessage;
    }

    @Override
    public String toString() {
        return "EmailNotifyMessage{" +
                "email='" + email + '\'' +
                ", title='" + title + '\'' +
                ", contents='" + contents.replace("\n", "").substring(0, 100) + '\'' +
                '}';
    }
}
