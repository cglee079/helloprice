package com.podo.helloprice.product.update.analysis.domain.notifylog.model;

import com.podo.helloprice.core.model.ProductSaleStatus;
import com.podo.helloprice.core.model.ProductUpdateStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product_notify_log")
@Entity
public class NotifyLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;

    @Enumerated(EnumType.STRING)
    private ProductUpdateStatus updateStatus;

    private LocalDateTime notifyAt;

    public NotifyLog(Long productId, ProductUpdateStatus updateStatus, LocalDateTime notifyAt) {
        this.productId = productId;
        this.updateStatus = updateStatus;
        this.notifyAt = notifyAt;
    }
}
