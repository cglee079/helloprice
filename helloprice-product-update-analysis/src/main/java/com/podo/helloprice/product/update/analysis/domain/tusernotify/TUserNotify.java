package com.podo.helloprice.product.update.analysis.domain.tusernotify;

import com.podo.helloprice.product.update.analysis.domain.productsale.ProductSale;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tuser_notify")
@Entity
public class TUserNotify {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tuser_id")
    private Long tUserId;

    @JoinColumn(name = "product_sale_id")
    @ManyToOne
    private ProductSale productSale;

    private LocalDateTime lastNotifiedAt;

    public void updateNotifiedAt(LocalDateTime notifyAt) {
        this.lastNotifiedAt = notifyAt;
    }
}
