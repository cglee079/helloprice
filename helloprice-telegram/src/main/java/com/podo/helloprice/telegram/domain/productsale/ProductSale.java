package com.podo.helloprice.telegram.domain.productsale;

import com.podo.helloprice.core.enums.SaleType;
import com.podo.helloprice.telegram.domain.BaseEntity;
import com.podo.helloprice.telegram.domain.product.model.Product;
import com.podo.helloprice.telegram.domain.userproduct.UserProductSaleNotify;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "product_sale")
public class ProductSale extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "product_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @Enumerated(EnumType.STRING)
    private SaleType saleType;

    private Integer price;

    private Integer prevPrice;

    private String additionalInfo;

    private LocalDateTime lastUpdateAt;

    @OneToMany(mappedBy = "productSale")
    private List<UserProductSaleNotify> userProductSaleNotifies = new ArrayList<>();

    public static ProductSale create(SaleType saleType, Integer price, String additionalInfo, LocalDateTime lastUpdateAt) {
        final ProductSale productSale = new ProductSale();
        productSale.saleType = saleType;
        productSale.price = price;
        productSale.prevPrice = price;
        productSale.lastUpdateAt = lastUpdateAt;
        productSale.additionalInfo = additionalInfo;
        return productSale;
    }

    public void setProduct(Product product) {
        this.product = product;
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

    public void addUserProductNotify(UserProductSaleNotify userProductSaleNotify) {
        this.userProductSaleNotifies.add(userProductSaleNotify);
        this.product.revive();
    }

    public void removeUserProductNotify(UserProductSaleNotify userProductSaleNotify) {
        this.userProductSaleNotifies.remove(userProductSaleNotify);
        this.product.checkSaleNotify();
    }

    public boolean hasAnyNotify() {
        return !this.userProductSaleNotifies.isEmpty();
    }

}

