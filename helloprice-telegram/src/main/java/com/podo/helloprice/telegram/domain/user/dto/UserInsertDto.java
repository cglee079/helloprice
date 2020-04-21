package com.podo.helloprice.telegram.domain.user.dto;


import com.podo.helloprice.core.model.UserStatus;
import com.podo.helloprice.telegram.app.menu.Menu;
import com.podo.helloprice.telegram.domain.user.model.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserInsertDto {

    private String telegramId;
    private String username;
    private String email;
    private Menu menuStatus;
    private UserStatus userStatus;
    private LocalDateTime lastSendAt;

    @Builder
    public UserInsertDto(String telegramId, String username, String email, Menu menuStatus, UserStatus userStatus, LocalDateTime lastSendAt) {
        this.telegramId = telegramId;
        this.username = username;
        this.email = email;
        this.menuStatus = menuStatus;
        this.userStatus = userStatus;
        this.lastSendAt = lastSendAt;
    }

    public User toEntity() {
        return User.builder()
                .telegramId(telegramId)
                .username(username)
                .email(email)
                .menuStatus(menuStatus)
                .userStatus(userStatus)
                .lastSendAt(lastSendAt)
                .errorCount(0)
                .build();
    }
}
