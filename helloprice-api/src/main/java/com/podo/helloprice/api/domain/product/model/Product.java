package com.podo.helloprice.api.domain.product.model;

import com.podo.helloprice.core.enums.SaleType;
import com.podo.helloprice.core.enums.ProductAliveStatus;
import com.podo.helloprice.core.enums.ProductSaleStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product")
@Entity
public class Product {

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

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @MapKey(name = "priceType")
    private Map<SaleType, ProductPrice> priceTypeToPrice = new HashMap<>();

}
