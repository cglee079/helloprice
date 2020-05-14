package com.podo.helloprice.product.update.analysis.domain.usernotify.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_notify")
@Entity
public class UserNotify{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id")
    private Long userId;

    @JoinColumn(name = "product_sale_id")
    private Long productSaleId;

    private LocalDateTime lastNotifiedAt;

    public UserNotify(Long userId, Long productSaleId){
        this.userId = userId;
        this.productSaleId = productSaleId;
    }

    public void updateLastNotifiedAt(LocalDateTime notifiedAt) {
        this.lastNotifiedAt = notifiedAt;
    }
}
