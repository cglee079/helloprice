package com.podo.helloprice.telegram.domain.userproduct;

import com.podo.helloprice.telegram.domain.BaseTimeEntity;
import com.podo.helloprice.telegram.domain.productsale.ProductSale;
import com.podo.helloprice.telegram.domain.user.model.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_product_sale_notify")
@Entity
public class UserProductSaleNotify extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_sale_id")
    private ProductSale productSale;

    private LocalDateTime lastNotifiedAt;

    public UserProductSaleNotify(User user, ProductSale productSale) {
        this.user = user;
        this.productSale = productSale;
    }
}
