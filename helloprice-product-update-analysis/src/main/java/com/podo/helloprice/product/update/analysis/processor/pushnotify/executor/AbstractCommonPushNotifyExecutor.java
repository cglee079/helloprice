package com.podo.helloprice.product.update.analysis.processor.pushnotify.executor;

import com.podo.helloprice.core.enums.UserStatus;
import com.podo.helloprice.product.update.analysis.domain.product.application.ProductReadService;
import com.podo.helloprice.product.update.analysis.domain.product.dto.ProductDto;
import com.podo.helloprice.product.update.analysis.domain.user.application.UserReadService;
import com.podo.helloprice.product.update.analysis.domain.user.dto.UserDto;
import com.podo.helloprice.product.update.analysis.domain.usernotify.application.UserNotifyReadService;
import com.podo.helloprice.product.update.analysis.processor.pushnotify.PushTarget;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
public abstract class AbstractCommonPushNotifyExecutor implements PushNotifyExecutor {

    @Autowired
    private ProductReadService productReadService;

    @Autowired
    private UserReadService userReadService;

    @Autowired
    private UserNotifyReadService userNotifyReadService;

    protected abstract String getContents(ProductDto product);

    @Override
    public PushTarget execute(Long productId) {

        final List<Long> userIds = userNotifyReadService.findUserIdsByProductId(productId);
        final List<UserDto> users = userReadService.findByIdsAndUserStatus(userIds, UserStatus.ALIVE);

        final ProductDto product = productReadService.findByProductId(productId);

        final String imageUrl = product.getImageUrl();
        final String contents = getContents(product);

        return new PushTarget(users, imageUrl, contents);
    }


}
