package com.podo.helloprice.core.domain.useritem;

import com.podo.helloprice.core.domain.BaseTimeEntity;
import com.podo.helloprice.core.domain.product.Product;
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
public class UserProductNotify extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Product item;

    private LocalDateTime lastNotifiedAt;

    public UserProductNotify(User user, Product item) {
        this.user = user;
        this.item = item;
    }

    public void updateNotifiedAt(LocalDateTime notifyAt) {
        this.lastNotifiedAt = notifyAt;
    }
}
