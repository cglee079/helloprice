package com.podo.sadream.telegram.domain.user;

import com.podo.sadream.core.domain.user.Menu;
import com.podo.sadream.core.domain.user.User;
import com.podo.sadream.core.domain.user.UserStatus;
import lombok.Builder;
import lombok.Getter;

public class UserDto {


    public static class insert {
        private Integer telegramId;
        private String username;
        private String email;
        private Menu menuStatus;
        private UserStatus userStatus;

        @Builder
        public insert(Integer telegramId, String username, String email, Menu menuStatus, UserStatus userStatus) {
            this.telegramId = telegramId;
            this.username = username;
            this.email = email;
            this.menuStatus = menuStatus;
            this.userStatus = userStatus;
        }

        public User toEntity() {
            return User.builder()
                    .telegramId(telegramId + "")
                    .username(username)
                    .email(email)
                    .menuStatus(menuStatus)
                    .userStatus(userStatus)
                    .errorCount(0)
                    .build();
        }
    }

    @Getter
    public static class detail {
        private Long id;
        private Integer telegramId;
        private String username;
        private String email;
        private Menu menuStatus;
        private UserStatus userStatus;

        public detail(User user) {
            this.id = user.getId();
            this.telegramId = Integer.valueOf(user.getTelegramId());
            this.username = user.getUsername();
            this.email = user.getEmail();
            this.menuStatus = user.getMenuStatus();
            this.userStatus = user.getUserStatus();
        }
    }
}
