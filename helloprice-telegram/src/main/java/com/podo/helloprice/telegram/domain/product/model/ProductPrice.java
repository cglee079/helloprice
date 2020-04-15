package com.podo.helloprice.telegram.domain.product.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "product_price")
public class ProductPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "product_id")
    @ManyToOne
    private Product product;

    @Enumerated(EnumType.STRING)
    private PriceType priceType;

    private Integer price;

    private Integer beforePrice;

    private String additionalInfo;

    private LocalDateTime lastUpdateAt;

    public static ProductPrice create(PriceType priceType, Integer price, String additionalInfo, LocalDateTime lastUpdateAt) {
        final ProductPrice productPrice = new ProductPrice();
        productPrice.priceType = priceType;
        productPrice.price = price;
        productPrice.beforePrice = 0;
        productPrice.lastUpdateAt = lastUpdateAt;
        productPrice.additionalInfo = additionalInfo;
        return productPrice;
    }

    public void update(Integer price, String additionalInfo, LocalDateTime updateAt) {
        final Integer existPrice = this.price;

        if (price.equals(existPrice) || this.additionalInfo.equals(additionalInfo)) {
            this.additionalInfo = additionalInfo;
            return ;
        }

        this.price = price;
        this.beforePrice = existPrice;
        this.additionalInfo = additionalInfo;
        this.lastUpdateAt = updateAt;
        return ;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}

