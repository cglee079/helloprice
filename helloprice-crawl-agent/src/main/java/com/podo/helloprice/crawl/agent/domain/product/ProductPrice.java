package com.podo.helloprice.crawl.agent.domain.product;

import com.podo.helloprice.core.enums.PriceType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "product_price")
@Getter
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

    private Integer prevPrice;

    private String additionalInfo;

    private LocalDateTime lastUpdateAt;

    @LastModifiedDate
    private LocalDateTime updateAt;

    @LastModifiedBy
    private String updateBy;

    public static ProductPrice create(PriceType priceType, Integer price, String additionalInfo, LocalDateTime lastUpdateAt) {
        final ProductPrice productPrice = new ProductPrice();
        productPrice.priceType = priceType;
        productPrice.price = price;
        productPrice.prevPrice = price;
        productPrice.lastUpdateAt = lastUpdateAt;
        productPrice.additionalInfo = additionalInfo;
        return productPrice;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public boolean update(Integer price, String additionalInfo, LocalDateTime updateAt) {
        if(Objects.isNull(price)){
            price = 0;
        }

        final Integer existPrice = this.price;

        if (price.equals(existPrice) && Objects.equals(this.additionalInfo, additionalInfo)) {
            this.additionalInfo = additionalInfo;
            return false;
        }

        this.price = price;
        this.prevPrice = existPrice;
        this.additionalInfo = additionalInfo;
        this.lastUpdateAt = updateAt;

        return true;
    }
}

