package com.podo.helloprice.notify.analysis.infra.mq.message;

import com.podo.helloprice.notify.analysis.notify.vo.TelegramNotify;
import lombok.*;

@AllArgsConstructor
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
