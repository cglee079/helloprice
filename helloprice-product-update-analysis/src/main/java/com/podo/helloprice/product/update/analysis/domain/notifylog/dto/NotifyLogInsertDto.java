package com.podo.helloprice.product.update.analysis.domain.notifylog.dto;


import com.podo.helloprice.core.enums.ProductUpdateStatus;
import com.podo.helloprice.product.update.analysis.domain.notifylog.model.NotifyLog;
import lombok.Builder;

import java.time.LocalDateTime;

public class NotifyLogInsertDto {

    private Long productId;
    private ProductUpdateStatus updateStatus;
    private LocalDateTime notifyAt;
    private String imageUrl;
    private String title;
    private String contents;

    public NotifyLog toEntity(){
        return new NotifyLog(productId, updateStatus, imageUrl, title, contents, notifyAt);
    }

    @Builder
    public NotifyLogInsertDto(Long productId,
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
