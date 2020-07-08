package com.podo.helloprice.product.update.analysis.domain.updatelog;

import com.podo.helloprice.core.enums.ProductAliveStatus;
import com.podo.helloprice.core.enums.ProductSaleStatus;
import com.podo.helloprice.core.enums.ProductUpdateStatus;
import com.podo.helloprice.product.update.analysis.domain.product.model.Product;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.LocalDateTime;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product_update_log")
@Entity
public class ProductUpdateLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;

    private String productName;

    private String productCode;

    private String url;

    private Integer price;

    private Integer prevPrice;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private ProductAliveStatus aliveStatus;

    @Enumerated(EnumType.STRING)
    private ProductSaleStatus saleStatus;

    @Enumerated(EnumType.STRING)
    private ProductUpdateStatus updateStatus;

    private LocalDateTime productUpdateAt;

    public ProductUpdateLog(Product product, Integer price, Integer prevPrice, ProductUpdateStatus updateStatus, LocalDateTime productUpdateAt) {
        this.productId = product.getId();
        this.productName = product.getProductName();
        this.productCode = product.getProductCode();
        this.url = product.getUrl();
        this.imageUrl = product.getImageUrl();
        this.price = price;
        this.prevPrice = prevPrice;
        this.aliveStatus = product.getAliveStatus();
        this.saleStatus = product.getSaleStatus();
        this.updateStatus = updateStatus;
        this.productUpdateAt = productUpdateAt;
    }

    public ProductUpdateLog(Product product, ProductUpdateStatus productUpdateStatus, LocalDateTime now) {
        this(product, null, null, productUpdateStatus, now);
    }
}
