package com.podo.helloprice.product.update.analysis.processor.notify.executor;

import com.podo.helloprice.core.enums.UserStatus;
import com.podo.helloprice.product.update.analysis.domain.product.application.ProductReadService;
import com.podo.helloprice.product.update.analysis.domain.product.dto.ProductSimpleDto;
import com.podo.helloprice.product.update.analysis.domain.user.UserReadService;
import com.podo.helloprice.product.update.analysis.domain.user.UserDto;
import com.podo.helloprice.product.update.analysis.domain.userproduct.application.UserProductNotifyReadService;
import com.podo.helloprice.product.update.analysis.infra.mq.message.EmailNotifyMessage;
import com.podo.helloprice.product.update.analysis.infra.mq.message.TelegramNotifyMessage;
import com.podo.helloprice.product.update.analysis.processor.notify.Notifier;
import com.podo.helloprice.product.update.analysis.processor.notify.NotifyTarget;
import com.podo.helloprice.product.update.analysis.processor.notify.helper.EmailContentCreator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
public abstract class AbstractCommonNotifyExecutor implements NotifyExecutor {

    @Autowired
    private ProductReadService productReadService;

    @Autowired
    private UserReadService userReadService;

    @Autowired
    private UserProductNotifyReadService userProductNotifyReadService;

    protected abstract String getNotifyTitle(ProductSimpleDto product);

    protected abstract String getNotifyContents(ProductSimpleDto product);

    @Override
    public NotifyTarget execute(Long productId) {

        final ProductSimpleDto product = productReadService.findByProductId(productId);

        final String imageUrl = product.getImageUrl();

        final List<Long> userIds = userProductNotifyReadService.findUserIdsByProductId(productId);
        final List<UserDto> users = userReadService.findByUserIdsAndUserStatus(userIds, UserStatus.ALIVE);

        final String title = getNotifyTitle(product);
        final String contents = getNotifyContents(product);

        return new NotifyTarget(users, imageUrl, title, contents);
    }


}
