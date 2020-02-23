package com.podo.helloprice.telegram.domain.user;

import com.podo.helloprice.core.model.Menu;
import com.podo.helloprice.core.domain.user.User;
import com.podo.helloprice.core.domain.user.model.UserStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class UserDto {


    public static class insert {
        private String telegramId;
        private String username;
        private String email;
        private Menu menuStatus;
        private UserStatus userStatus;
        private LocalDateTime lastSendAt;

        @Builder
        public insert(String telegramId, String username, String email, Menu menuStatus, UserStatus userStatus, LocalDateTime lastSendAt) {
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

    @Getter
    public static class detail {
        private Long id;
        private String telegramId;
        private String username;
        private String email;
        private Menu menuStatus;
        private UserStatus userStatus;

        public detail(User user) {
            this.id = user.getId();
            this.telegramId = user.getTelegramId();
            this.username = user.getUsername();
            this.email = user.getEmail();
            this.menuStatus = user.getMenuStatus();
            this.userStatus = user.getUserStatus();
        }
    }
}
