package com.podo.helloprice.product.update.analysis.domain.tuser;

import lombok.Getter;

@Getter
public class TUserDto {

    private String telegramId;
    private String username;
    private String email;

    public TUserDto(String telegramId, String username, String email) {
        this.telegramId = telegramId;
        this.username = username;
        this.email = email;
    }
}
