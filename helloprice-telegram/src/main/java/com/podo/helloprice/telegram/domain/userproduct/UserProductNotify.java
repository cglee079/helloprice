package com.podo.helloprice.telegram.domain.userproduct;

import com.podo.helloprice.telegram.domain.BaseTimeEntity;
import com.podo.helloprice.telegram.domain.product.Product;
import com.podo.helloprice.telegram.domain.user.model.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_product_notify")
@Entity
public class UserProductNotify extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private LocalDateTime lastNotifiedAt;

    public UserProductNotify(User user, Product product) {
        this.user = user;
        this.product = product;
    }

    public void updateNotifiedAt(LocalDateTime notifyAt) {
        this.lastNotifiedAt = notifyAt;
    }
}
