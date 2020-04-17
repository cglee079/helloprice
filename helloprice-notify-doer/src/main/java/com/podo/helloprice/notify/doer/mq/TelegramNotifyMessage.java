package com.podo.helloprice.notify.doer.mq;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class TelegramNotifyMessage {

    private String telegramId;
    private String contents;

    @Override
    public String toString() {
        return "TelegramNotifyMessage{" +
                "telegramId='" + telegramId + '\'' +
                ", contents='" + contents.replace("\n", "").substring(0, 100) + '\'' +
                '}';
    }
}
