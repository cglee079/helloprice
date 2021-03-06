package com.podo.helloprice.product.update.analysis.domain.productsale;

import com.podo.helloprice.core.enums.SaleType;
import com.podo.helloprice.product.update.analysis.domain.product.model.Product;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "product_sale")
public class ProductSale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "product_id")
    @ManyToOne
    private Product product;

    @Enumerated(EnumType.STRING)
    private SaleType saleType;

    private Integer price;

    private Integer prevPrice;

    private String additionalInfo;

    private LocalDateTime lastUpdateAt;
}

