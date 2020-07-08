package com.podo.helloprice.telegram.domain.tusernotify;

import com.podo.helloprice.telegram.domain.BaseTimeEntity;
import com.podo.helloprice.telegram.domain.productsale.ProductSale;
import com.podo.helloprice.telegram.domain.tuser.model.TUser;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tuser_notify")
@Entity
public class TUserNotify extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tuser_id")
    private TUser tUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_sale_id")
    private ProductSale productSale;

    private LocalDateTime lastNotifiedAt;

    public TUserNotify(TUser tUser, ProductSale productSale) {
        this.tUser = tUser;
        this.productSale = productSale;
    }
}
