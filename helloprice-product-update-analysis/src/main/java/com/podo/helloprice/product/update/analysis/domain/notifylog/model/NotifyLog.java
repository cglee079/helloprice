package com.podo.helloprice.product.update.analysis.domain.notifylog.model;

import com.podo.helloprice.core.enums.ProductUpdateStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
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

    private String imageUrl;

    private String title;

    private String contents;

    private LocalDateTime notifyAt;

    public NotifyLog(Long productId,
                     ProductUpdateStatus updateStatus,
                     String imageUrl,
                     String title,
                     String contents,
                     LocalDateTime notifyAt) {
        this.productId = productId;
        this.updateStatus = updateStatus;
        this.imageUrl = imageUrl;
        this.title = title;
        this.contents = contents;
        this.notifyAt = notifyAt;
    }
}
