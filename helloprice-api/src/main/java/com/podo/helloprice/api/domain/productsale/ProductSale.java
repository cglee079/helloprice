package com.podo.helloprice.api.domain.productsale;

import com.podo.helloprice.api.domain.product.model.Product;
import com.podo.helloprice.core.enums.SaleType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "product_sale")
public class ProductSale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "product_id")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Product product;

    @Enumerated(EnumType.STRING)
    private SaleType saleType;

    private Integer price;

    private Integer prevPrice;

    private String additionalInfo;

    private LocalDateTime lastUpdateAt;

    public static ProductSale create(Product product, SaleType saleType, Integer price, String additionalInfo, LocalDateTime lastUpdateAt) {
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

        if(Objects.isNull(this.price)){
            this.price = 0;
        }

        final Integer existPrice = this.price;

        if (price.equals(existPrice) && Objects.equals(this.additionalInfo, additionalInfo)) {
            return false;
        }

        this.price = price;
        this.prevPrice = existPrice;
        this.additionalInfo = additionalInfo;
        this.lastUpdateAt = updateAt;

        return true;
    }

}

