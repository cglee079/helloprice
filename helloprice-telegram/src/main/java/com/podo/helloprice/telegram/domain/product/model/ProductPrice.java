package com.podo.helloprice.telegram.domain.product.model;

import com.podo.helloprice.core.model.PriceType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

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
        productPrice.beforePrice = price;
        productPrice.lastUpdateAt = lastUpdateAt;
        productPrice.additionalInfo = additionalInfo;
        return productPrice;
    }

    public void update(Integer price, String additionalInfo, LocalDateTime updateAt) {

        if(Objects.isNull(price)){
            price = 0;
        }

        final Integer existPrice = this.price;

        if (price.equals(existPrice) && Objects.equals(this.additionalInfo, additionalInfo)) {
            return ;
        }

        this.price = price;
        this.beforePrice = existPrice;
        this.additionalInfo = additionalInfo;
        this.lastUpdateAt = updateAt;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}

