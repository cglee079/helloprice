package com.podo.helloprice.telegram.job.notifier;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class NotifyUserVo {

    private String username;
    private String email;
    private String telegramId;

    public NotifyUserVo(String username, String email, String telegramId) {
        this.username = username;
        this.email = email;
        this.telegramId = telegramId;
    }

}
