package com.podo.helloprice.telegram.domain.tuser.dto;


import com.podo.helloprice.core.enums.UserStatus;
import com.podo.helloprice.telegram.app.menu.Menu;
import com.podo.helloprice.telegram.domain.tuser.model.TUser;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TUserInsertDto {

    private String telegramId;
    private String username;
    private String email;
    private Menu menuStatus;
    private UserStatus userStatus;
    private LocalDateTime lastSendAt;

    @Builder
    public TUserInsertDto(String telegramId, String username, String email, Menu menuStatus, UserStatus userStatus, LocalDateTime lastSendAt) {
        this.telegramId = telegramId;
        this.username = username;
        this.email = email;
        this.menuStatus = menuStatus;
        this.userStatus = userStatus;
        this.lastSendAt = lastSendAt;
    }

    public TUser toEntity() {
        return TUser.builder()
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
