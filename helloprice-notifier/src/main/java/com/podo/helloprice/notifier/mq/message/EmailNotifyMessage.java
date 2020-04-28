package com.podo.helloprice.notifier.mq.message;

import com.podo.helloprice.core.util.StringUtil;
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

    @Override
    public String toString() {
        return "EmailNotifyMessage{" +
                "email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", title='" + title + '\'' +
                ", contents='" + StringUtil.summary(contents, 100) + '\'' +
                '}';
    }
}
