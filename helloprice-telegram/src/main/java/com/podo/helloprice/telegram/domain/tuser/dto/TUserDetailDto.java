package com.podo.helloprice.telegram.domain.tuser.dto;

import com.podo.helloprice.telegram.app.menu.Menu;
import com.podo.helloprice.telegram.domain.tuser.model.TUser;
import com.podo.helloprice.core.enums.UserStatus;
import lombok.Getter;

@Getter
public class TUserDetailDto {
    private Long id;
    private String telegramId;
    private String username;
    private String email;
    private Menu menuStatus;
    private UserStatus userStatus;

    public TUserDetailDto(TUser tUser) {
        this.id = tUser.getId();
        this.telegramId = tUser.getTelegramId();
        this.username = tUser.getUsername();
        this.email = tUser.getEmail();
        this.menuStatus = tUser.getMenuStatus();
        this.userStatus = tUser.getUserStatus();
    }

}
