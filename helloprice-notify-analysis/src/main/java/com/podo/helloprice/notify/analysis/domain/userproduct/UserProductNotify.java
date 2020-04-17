package com.podo.helloprice.notify.analysis.domain.userproduct;

import com.podo.helloprice.core.model.PriceType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_product_notify")
@Entity
public class UserProductNotify{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long productId;

    @Enumerated(EnumType.STRING)
    private PriceType priceType;

    private LocalDateTime lastNotifiedAt;

    public void updateNotifiedAt(LocalDateTime notifyAt) {
        this.lastNotifiedAt = notifyAt;
    }
}
