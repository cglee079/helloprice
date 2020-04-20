package com.podo.helloprice.crawl.agent.domain.product;

import com.podo.helloprice.core.model.PriceType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private Integer beforePrice;

    private String additionalInfo;

    private LocalDateTime lastUpdateAt;

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
        this.beforePrice = existPrice;
        this.additionalInfo = additionalInfo;
        this.lastUpdateAt = updateAt;

        return true;
    }
}

