package com.podo.helloprice.product.update.analysis.processor.notify.executor;

import com.podo.helloprice.core.model.PriceType;
import com.podo.helloprice.core.model.ProductUpdateStatus;
import com.podo.helloprice.product.update.analysis.processor.notify.NotifyExecutor;
import com.podo.helloprice.product.update.analysis.domain.product.dto.ProductDetailDto;
import com.podo.helloprice.product.update.analysis.domain.user.dto.UserDto;
import com.podo.helloprice.product.update.analysis.facade.NotifyTarget;
import com.podo.helloprice.product.update.analysis.facade.NotifyTargetFacadeReadService;
import com.podo.helloprice.product.update.analysis.infra.mq.message.EmailNotifyMessage;
import com.podo.helloprice.product.update.analysis.infra.mq.message.TelegramNotifyMessage;
import com.podo.helloprice.product.update.analysis.processor.notify.Notifier;
import com.podo.helloprice.product.update.analysis.processor.notify.executor.helper.EmailContentCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.podo.helloprice.core.model.ProductUpdateStatus.UPDATE_SALE_CARD_PRICE;
import static com.podo.helloprice.product.update.analysis.processor.notify.executor.helper.SaleNotifyChecker.satifiedSendNotify;
import static com.podo.helloprice.product.update.analysis.processor.notify.executor.helper.SaleNotifyHelper.contents;
import static com.podo.helloprice.product.update.analysis.processor.notify.executor.helper.SaleNotifyHelper.title;

@RequiredArgsConstructor
@Component
public class SaleOfCardProcessor implements NotifyExecutor {

    private final NotifyTargetFacadeReadService notifyTargetFacadeReadService;
    private final Notifier notifier;

    @Override
    public ProductUpdateStatus getProductUpdateStatus() {
        return UPDATE_SALE_CARD_PRICE;
    }

    @Override
    public void execute(Long productId) {
        final NotifyTarget notifyTarget = notifyTargetFacadeReadService.get(productId, PriceType.CARD);
        final ProductDetailDto product = notifyTarget.getProduct();

        final String imageUrl = product.getImageUrl();
        final String title = title(product);
        final String contents = contents(product);

        if (satifiedSendNotify(product.getPrice(), product.getBeforePrice())) {
            for (UserDto user : notifyTarget.getUsers()) {
                notifier.notify(TelegramNotifyMessage.create(user.getTelegramId(), imageUrl, contents));
                notifier.notify(EmailNotifyMessage.create(user.getEmail(), user.getUsername(), title, EmailContentCreator.create(imageUrl, contents)));
            }
        }
    }
}
