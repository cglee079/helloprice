package com.podo.helloprice.api.domain.userproduct;

import com.podo.helloprice.api.domain.product.model.Product;
import com.podo.helloprice.api.domain.productsale.ProductSale;
import com.podo.helloprice.core.enums.SaleType;
import com.podo.helloprice.api.domain.user.model.User;
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

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    @JoinColumn(name = "product_sale_id")
    @ManyToOne(fetch = FetchType.LAZY, optional =  false)
    private ProductSale productSale;

    private LocalDateTime lastNotifiedAt;
}
