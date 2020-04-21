package com.podo.helloprice.product.update.analysis.processor.notify.executor;

import com.podo.helloprice.core.model.PriceType;
import com.podo.helloprice.product.update.analysis.domain.product.dto.ProductDetailDto;
import com.podo.helloprice.product.update.analysis.domain.user.dto.UserDto;
import com.podo.helloprice.product.update.analysis.facade.NotifyTarget;
import com.podo.helloprice.product.update.analysis.facade.NotifyTargetFacadeReadService;
import com.podo.helloprice.product.update.analysis.infra.mq.message.EmailNotifyMessage;
import com.podo.helloprice.product.update.analysis.infra.mq.message.TelegramNotifyMessage;
import com.podo.helloprice.product.update.analysis.processor.notify.Notifier;
import com.podo.helloprice.product.update.analysis.processor.notify.helper.EmailContentCreator;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class CommonNotifyExecutor implements NotifyExecutor {

    @Autowired
    private Notifier notifier;

    @Autowired
    private NotifyTargetFacadeReadService notifyTargetFacadeReadService;

    protected abstract String getNotifyTitle(ProductDetailDto product);

    protected abstract String getNotifyContents(ProductDetailDto product);

    @Override
    public boolean execute(Long productId) {

        for (PriceType value : PriceType.values()) {

            final NotifyTarget notifyTarget = notifyTargetFacadeReadService.get(productId, value);
            final ProductDetailDto product = notifyTarget.getProduct();

            final String imageUrl = product.getImageUrl();
            final String title = getNotifyTitle(product);
            final String contents = getNotifyContents(product);

            for (UserDto user : notifyTarget.getUsers()) {
                notifier.notify(TelegramNotifyMessage.create(user.getTelegramId(), imageUrl, contents));
                notifier.notify(EmailNotifyMessage.create(user.getEmail(), user.getUsername(), title, EmailContentCreator.create(imageUrl, contents)));
            }

        }

        return true;
    }


}
