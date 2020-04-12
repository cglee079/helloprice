package com.podo.helloprice.telegram.domain.user.dto;

import com.podo.helloprice.telegram.app.menu.Menu;
import com.podo.helloprice.telegram.domain.user.model.User;
import com.podo.helloprice.telegram.domain.user.model.UserStatus;
import lombok.Getter;

@Getter
public class UserDetailDto {
    private Long id;
    private String telegramId;
    private String username;
    private String email;
    private Menu menuStatus;
    private UserStatus userStatus;

    public UserDetailDto(User user) {
        this.id = user.getId();
        this.telegramId = user.getTelegramId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.menuStatus = user.getMenuStatus();
        this.userStatus = user.getUserStatus();
    }

}
