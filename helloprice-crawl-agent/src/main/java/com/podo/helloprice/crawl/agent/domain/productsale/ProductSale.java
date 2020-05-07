package com.podo.helloprice.crawl.agent.domain.productsale;

import com.podo.helloprice.core.enums.SaleType;
import com.podo.helloprice.crawl.agent.domain.product.Product;
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
@Table(name = "product_sale")
@Getter
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

    @LastModifiedDate
    private LocalDateTime updateAt;

    @LastModifiedBy
    private String updateBy;

    public static ProductSale create(SaleType saleType, Product product, Integer price, String additionalInfo, LocalDateTime lastUpdateAt) {
        final ProductSale productSale = new ProductSale();
        productSale.product = product;
        productSale.saleType = saleType;
        productSale.price = price;
        productSale.prevPrice = price;
        productSale.lastUpdateAt = lastUpdateAt;
        productSale.additionalInfo = additionalInfo;
        return productSale;
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

