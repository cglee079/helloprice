package com.podo.helloprice.notify.analysis.domain.product.model;

import com.podo.helloprice.core.model.PriceType;
import com.podo.helloprice.core.model.ProductAliveStatus;
import com.podo.helloprice.core.model.ProductSaleStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product")
@Entity
public class Product{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productCode;

    private String productName;

    private String description;

    private String url;

    private String imageUrl;

    private LocalDateTime lastCrawledAt;

    private LocalDateTime lastPublishAt;

    private Integer deadCount;

    @Enumerated(EnumType.STRING)
    private ProductAliveStatus aliveStatus;

    @Enumerated(EnumType.STRING)
    private ProductSaleStatus saleStatus;

    @OneToMany(mappedBy = "product")
    @MapKey(name = "priceType")
    private Map<PriceType, ProductPrice> productPrices = new HashMap<>();

    public Integer getPrice(PriceType priceType) {
        return this.productPrices.get(priceType).getPrice();
    }

    public Integer getBeforePrice(PriceType priceType) {
        return this.productPrices.get(priceType).getBeforePrice();
    }

    public LocalDateTime getLastUpdateAt(PriceType priceType) {
        return this.productPrices.get(priceType).getLastUpdateAt();
    }

    public String getPriceAdditionalInfo(PriceType priceType) {
        return this.productPrices.get(priceType).getAdditionalInfo();
    }

}
