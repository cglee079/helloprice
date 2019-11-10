package com.podo.sadream.core.domain.user;

import com.podo.sadream.core.domain.UpdatableBaseEntity;
import com.podo.sadream.core.domain.useritem.UserItemNotify;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

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

    private Integer errorCount;

    @Enumerated(EnumType.STRING)
    private Menu menuStatus;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @OneToMany(mappedBy = "user")
    List<UserItemNotify> userItemNotifies;

    @Builder
    public User(String username,
                String telegramId, String email, Integer errorCount,
                Menu menuStatus, UserStatus userStatus) {
        this.username = username;
        this.telegramId = telegramId;
        this.email = email;
        this.errorCount = errorCount;
        this.menuStatus = menuStatus;
        this.userStatus = userStatus;
    }

    public void updateMenuStatus(Menu menuStatus) {
        this.menuStatus = menuStatus;
    }

    public void addUserItemNotify(UserItemNotify userItemNotify) {
        this.userItemNotifies.add(userItemNotify);
    }

    public void deleteUserItemNotify(UserItemNotify userItemNotify) {
        this.userItemNotifies.remove(userItemNotify);
    }

    public void increaseErrorCount() {
        errorCount++;
    }

    public void resetErrorCount() {
        this.errorCount = 0;
    }

    public void died() {
        this.userStatus = UserStatus.DEAD;
    }

    public void revive() {
        this.userStatus = UserStatus.ALIVE;
        this.errorCount = 0;
    }

}