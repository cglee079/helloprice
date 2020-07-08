package com.podo.helloprice.product.update.analysis.infra.mq.message;

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

    public static TelegramNotifyMessage create(String telegramId, String imageUrl, String contents){
        final TelegramNotifyMessage telegramNotifyMessage = new TelegramNotifyMessage();
        telegramNotifyMessage.telegramId = telegramId;
        telegramNotifyMessage.imageUrl = imageUrl;
        telegramNotifyMessage.contents = contents;
        return telegramNotifyMessage;
    }

    @Override
    public String toString() {
        return "TelegramNotifyMessage{" +
                "telegramId='" + telegramId + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", contents='" + StringUtil.summary(contents, 100) + '\'' +
                '}';
    }
}
