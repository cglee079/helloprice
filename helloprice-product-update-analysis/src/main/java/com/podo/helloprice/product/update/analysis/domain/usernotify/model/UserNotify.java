package com.podo.helloprice.product.update.analysis.domain.usernotify.model;

import com.podo.helloprice.product.update.analysis.domain.BaseTimeEntity;
import com.podo.helloprice.product.update.analysis.domain.productsale.ProductSale;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_notify")
@Entity
public class UserNotify extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

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
