package com.podo.helloprice.crawl.agent.global.infra.mq.message;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotifyEvent {

    private Long productId;
    private UpdateStatus updateStatus;

    public NotifyEvent(Long productId, UpdateStatus updateStatus) {
        this.productId = productId;
        this.updateStatus = updateStatus;
    }
}
