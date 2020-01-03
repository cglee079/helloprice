package com.podo.helloprice.core.domain.useritem;

import com.podo.helloprice.core.domain.BaseEntity;
import com.podo.helloprice.core.domain.item.Item;
import com.podo.helloprice.core.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_item_notify")
@Entity
public class UserItemNotify extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private LocalDateTime lastNotifiedAt;

    public UserItemNotify(User user, Item item) {
        this.user = user;
        this.item = item;
    }

    public void updateNotifiedAt(LocalDateTime notifyAt) {
        this.lastNotifiedAt = notifyAt;
    }
}
