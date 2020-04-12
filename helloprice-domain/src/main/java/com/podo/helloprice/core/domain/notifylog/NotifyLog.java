package com.podo.helloprice.core.domain.notifylog;

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

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;

    private String url;

    private Integer price;

    private Integer beforePrice;

    private BigDecimal priceChangeRate;

    private String aliveStatus;

    private LocalDateTime notifyAt;

    @Builder
    public NotifyLog(String productName, String url, Integer price, Integer beforePrice, BigDecimal priceChangeRate, String aliveStatus, LocalDateTime notifyAt) {
        this.productName = productName;
        this.url = url;
        this.price = price;
        this.beforePrice = beforePrice;
        this.priceChangeRate = priceChangeRate;
        this.aliveStatus = aliveStatus;
        this.notifyAt = notifyAt;
    }
}
