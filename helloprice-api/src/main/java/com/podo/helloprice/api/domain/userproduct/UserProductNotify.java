package com.podo.helloprice.api.domain.userproduct;

import com.podo.helloprice.api.domain.product.model.Product;
import com.podo.helloprice.core.enums.SaleType;
import com.podo.helloprice.api.domain.user.model.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_product_notify")
@Entity
public class UserProductNotify{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Enumerated(EnumType.STRING)
    private SaleType saleType;

    private LocalDateTime lastNotifiedAt;
}
