package com.podo.helloprice.telegram.app.value;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
public class MessageVo {

    private String telegramId;
    private Integer messageId;

    public MessageVo(String telegramId, Integer messageId) {
        this.telegramId = telegramId;
        this.messageId = messageId;
    }
}

