package com.podo.helloprice.notifier.mq.message;

import com.podo.helloprice.core.util.StringUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class TelegramNotifyMessage {

    private String telegramId;
    private String imageUrl;
    private String contents;

    @Override
    public String toString() {
        return "TelegramNotifyMessage{" +
                "telegramId='" + telegramId + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", contents='" + StringUtil.summary(contents, 100) + '\'' +
                '}';
    }
}
