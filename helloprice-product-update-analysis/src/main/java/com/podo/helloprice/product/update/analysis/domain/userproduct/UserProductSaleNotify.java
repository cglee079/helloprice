package com.podo.helloprice.product.update.analysis.domain.userproduct;

import com.podo.helloprice.product.update.analysis.domain.productsale.ProductSale;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_product_sale_notify")
@Entity
public class UserProductSaleNotify {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @JoinColumn(name = "product_sale_id")
    @ManyToOne
    private ProductSale productSale;

    private LocalDateTime lastNotifiedAt;

    public void updateNotifiedAt(LocalDateTime notifyAt) {
        this.lastNotifiedAt = notifyAt;
    }
}
