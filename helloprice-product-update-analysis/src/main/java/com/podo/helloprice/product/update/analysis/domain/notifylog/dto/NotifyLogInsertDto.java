package com.podo.helloprice.product.update.analysis.domain.notifylog.dto;


import com.podo.helloprice.core.model.ProductUpdateStatus;
import com.podo.helloprice.product.update.analysis.domain.notifylog.model.NotifyLog;

import java.time.LocalDateTime;

public class NotifyLogInsertDto {

    private Long productId;
    private ProductUpdateStatus updateStatus;
    private LocalDateTime notifyAt;

    public NotifyLog toEntity(){
        return new NotifyLog(productId, updateStatus, notifyAt);
    }

    public NotifyLogInsertDto(Long productId, ProductUpdateStatus updateStatus, LocalDateTime notifyAt) {
        this.productId = productId;
        this.updateStatus = updateStatus;
        this.notifyAt = notifyAt;
    }
}
