package com.podo.helloprice.product.update.analysis.domain.user.dto;

import lombok.Getter;

@Getter
public class UserDto {

    private String telegramId;
    private String username;
    private String email;

    public UserDto(String telegramId, String username, String email) {
        this.telegramId = telegramId;
        this.username = username;
        this.email = email;
    }
}
