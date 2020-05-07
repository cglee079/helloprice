package com.podo.helloprice.telegram.domain.user.model;

import com.podo.helloprice.core.enums.UserStatus;
import com.podo.helloprice.telegram.app.menu.Menu;
import com.podo.helloprice.telegram.domain.BaseEntity;
import com.podo.helloprice.telegram.domain.userproduct.UserProductSaleNotify;
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
public class User extends BaseEntity {

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

    @OneToMany(mappedBy = "user")
    List<UserProductSaleNotify> userProductNotifies;

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
        this.lastSendAt = lastSendAt;
    }

    public void updateLastSendAt(LocalDateTime lastSendAt) {
        this.lastSendAt = lastSendAt;
    }

    public void updateMenuStatus(Menu menuStatus) {
        this.menuStatus = menuStatus;
    }

    public void addUserProductNotify(UserProductSaleNotify userProductSaleNotify) {
        this.userProductNotifies.add(userProductSaleNotify);
    }

    public void removeUserProductNotify(UserProductSaleNotify userProductSaleNotify) {
        this.userProductNotifies.remove(userProductSaleNotify);
    }

    public void increaseErrorCount(Integer userMaxErrorCount) {
        errorCount++;

        if ( this.errorCount > userMaxErrorCount) {
            this.userStatus = UserStatus.DEAD;
        }
    }

    public void clearErrorCount() {
        this.errorCount = 0;
    }

    public void revive() {
        this.userStatus = UserStatus.ALIVE;
        this.errorCount = 0;
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public boolean hasProductNotifiesMoreThan(Integer maxCountOfProductNotifies) {
        return this.userProductNotifies.size() >= maxCountOfProductNotifies;
    }
}
