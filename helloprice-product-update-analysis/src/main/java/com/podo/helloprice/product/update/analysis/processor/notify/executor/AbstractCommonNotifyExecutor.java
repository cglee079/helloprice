package com.podo.helloprice.product.update.analysis.processor.notify.executor;

import com.podo.helloprice.core.enums.UserStatus;
import com.podo.helloprice.product.update.analysis.domain.product.application.ProductReadService;
import com.podo.helloprice.product.update.analysis.domain.product.dto.ProductDto;
import com.podo.helloprice.product.update.analysis.domain.tuser.TUserDto;
import com.podo.helloprice.product.update.analysis.domain.tuser.TUserReadService;
import com.podo.helloprice.product.update.analysis.domain.tusernotify.application.TUserNotifyReadService;
import com.podo.helloprice.product.update.analysis.processor.notify.NotifyTarget;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
public abstract class AbstractCommonNotifyExecutor implements NotifyExecutor {

    @Autowired
    private ProductReadService productReadService;

    @Autowired
    private TUserReadService tUserReadService;

    @Autowired
    private TUserNotifyReadService tUserNotifyReadService;

    protected abstract String getNotifyTitle(ProductDto product);

    protected abstract String getNotifyContents(ProductDto product);

    @Override
    public NotifyTarget execute(Long productId) {

        List<Long> userIds = tUserNotifyReadService.findUserIdsByProductId(productId);
        final List<TUserDto> users = tUserReadService.findByUserIdsAndUserStatus(userIds, UserStatus.ALIVE);

        final ProductDto product = productReadService.findByProductId(productId);

        final String imageUrl = product.getImageUrl();
        final String title = getNotifyTitle(product);
        final String contents = getNotifyContents(product);

        return new NotifyTarget(users, imageUrl, title, contents);
    }


}
