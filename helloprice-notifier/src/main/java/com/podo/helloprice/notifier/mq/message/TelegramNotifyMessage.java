package com.podo.helloprice.notifier.mq.message;

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
                ", contents='" + contents.replace("\n", "").substring(0, 100) + '\'' +
                '}';
    }
}
