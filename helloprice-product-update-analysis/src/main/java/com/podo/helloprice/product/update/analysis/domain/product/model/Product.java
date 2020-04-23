package com.podo.helloprice.product.update.analysis.domain.product.model;

import com.podo.helloprice.core.enums.PriceType;
import com.podo.helloprice.core.enums.ProductAliveStatus;
import com.podo.helloprice.core.enums.ProductSaleStatus;
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
    private Map<PriceType, ProductPrice> priceTypeToPrice = new HashMap<>();

    public ProductPrice getProductPriceByType(PriceType priceType){
        return priceTypeToPrice.get(priceType);
    }

}
