package com.podo.helloprice.core.domain.user;

import com.podo.helloprice.core.domain.UpdatableBaseEntity;
import com.podo.helloprice.core.domain.useritem.UserItemNotify;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
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

    private LocalDateTime lastSendAt;

    private LocalDateTime lastNotifyAt;

    @OneToMany(mappedBy = "user")
    List<UserItemNotify> userItemNotifies;

    @Builder
    public User(String username,
                String telegramId, String email, Integer errorCount,
                Menu menuStatus, UserStatus userStatus, LocalDateTime lastSendAt) {
        this.username = username;
        this.telegramId = telegramId;
        this.email = email;
        this.errorCount = errorCount;
        this.menuStatus = menuStatus;
        this.userStatus = userStatus;
    }

    public void updateNotifyAt(LocalDateTime lastNotifyAt) {
        this.lastNotifyAt = lastNotifyAt;
    }

    public void updateSendAt(LocalDateTime lastSendAt) {
        this.lastSendAt = lastSendAt;
    }

    public void updateMenuStatus(Menu menuStatus) {
        this.menuStatus = menuStatus;
    }

    public void addUserItemNotify(UserItemNotify userItemNotify) {
        this.userItemNotifies.add(userItemNotify);
    }

    public void removeUserItemNotify(UserItemNotify userItemNotify) {
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