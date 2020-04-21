package com.podo.helloprice.product.update.analysis.processor.notify.executor;

import com.podo.helloprice.core.model.UserStatus;
import com.podo.helloprice.product.update.analysis.domain.product.application.ProductReadService;
import com.podo.helloprice.product.update.analysis.domain.product.dto.ProductSimpleDto;
import com.podo.helloprice.product.update.analysis.domain.user.UserReadService;
import com.podo.helloprice.product.update.analysis.domain.user.dto.UserDto;
import com.podo.helloprice.product.update.analysis.domain.userproduct.application.UserProductNotifyReadService;
import com.podo.helloprice.product.update.analysis.infra.mq.message.EmailNotifyMessage;
import com.podo.helloprice.product.update.analysis.infra.mq.message.TelegramNotifyMessage;
import com.podo.helloprice.product.update.analysis.processor.notify.Notifier;
import com.podo.helloprice.product.update.analysis.processor.notify.helper.EmailContentCreator;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class AbstractCommonNotifyExecutor implements NotifyExecutor {

    @Autowired
    private Notifier notifier;

    @Autowired
    private ProductReadService productReadService;

    @Autowired
    private UserReadService userReadService;

    @Autowired
    private UserProductNotifyReadService userProductNotifyReadService;

    protected abstract String getNotifyTitle(ProductSimpleDto product);

    protected abstract String getNotifyContents(ProductSimpleDto product);

    @Override
    public boolean execute(Long productId) {

        final ProductSimpleDto product = productReadService.findByProductId(productId);

        final String imageUrl = product.getImageUrl();

        final List<Long> userIds = userProductNotifyReadService.findUserIdsByProductId(productId);
        final List<UserDto> users = userReadService.findByUserIdsAndUserStatus(userIds, UserStatus.ALIVE);

        final String title = getNotifyTitle(product);
        final String contents = getNotifyContents(product);

        for (UserDto user : users) {
            notifier.notify(TelegramNotifyMessage.create(user.getTelegramId(), imageUrl, contents));
            notifier.notify(EmailNotifyMessage.create(user.getEmail(), user.getUsername(), title, EmailContentCreator.create(imageUrl, contents)));
        }

        return true;
    }


}
