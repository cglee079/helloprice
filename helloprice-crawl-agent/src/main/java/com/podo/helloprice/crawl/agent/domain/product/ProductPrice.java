package com.podo.helloprice.crawl.agent.domain.product;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    private LocalDateTime lastUpdateAt;

    public boolean update(Integer price, LocalDateTime updateAt) {
        final Integer existPrice = this.price;

        if (price.equals(existPrice)) {
            return false;
        }

        this.price = price;
        this.beforePrice = existPrice;
        this.lastUpdateAt = updateAt;

        return true;
    }
}

