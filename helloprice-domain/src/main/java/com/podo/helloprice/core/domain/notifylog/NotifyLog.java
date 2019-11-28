package com.podo.helloprice.core.domain.notifylog;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.LocalDateTime;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "notify_log")
@Entity
public class NotifyLog {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemName;

    private String itemUrl;

    private Integer itemPrice;

    private Integer itemBeforePrice;

    private Double itemPriceChangeRate;

    private String itemStatus;

    private LocalDateTime notifyAt;

    @Builder
    public NotifyLog(String itemName, String itemUrl, Integer itemPrice, Integer itemBeforePrice, Double itemPriceChangeRate, String itemStatus, LocalDateTime notifyAt) {
        this.itemName = itemName;
        this.itemUrl = itemUrl;
        this.itemPrice = itemPrice;
        this.itemBeforePrice = itemBeforePrice;
        this.itemPriceChangeRate = itemPriceChangeRate;
        this.itemStatus = itemStatus;
        this.notifyAt = notifyAt;
    }
}
