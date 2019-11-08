package com.podo.itemwatcher.core.domain.user;

import com.podo.itemwatcher.core.domain.UpdatableBaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user")
@Entity
public class User extends UpdatableBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String telegramId;

    private String email;

    @Enumerated(EnumType.STRING)
    private MenuStatus menuStatus;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @Builder
    public User(String username,
                String telegramId, String email,
                MenuStatus menuStatus, UserStatus userStatus) {
        this.username = username;
        this.telegramId = telegramId;
        this.email = email;
        this.menuStatus = menuStatus;
        this.userStatus = userStatus;
    }

    public void updateMenuStatus(MenuStatus menuStatus) {
        this.menuStatus = menuStatus;
    }
}