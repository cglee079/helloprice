package com.podo.helloprice.product.update.analysis.processor.notify.executor;

import com.podo.helloprice.core.model.PriceType;
import com.podo.helloprice.core.model.ProductUpdateStatus;
import com.podo.helloprice.product.update.analysis.notify.contents.NotifyContents;
import com.podo.helloprice.product.update.analysis.processor.notify.NotifyExecutor;
import com.podo.helloprice.product.update.analysis.domain.product.dto.ProductDetailDto;
import com.podo.helloprice.product.update.analysis.domain.user.dto.UserDto;
import com.podo.helloprice.product.update.analysis.facade.NotifyTarget;
import com.podo.helloprice.product.update.analysis.facade.NotifyTargetFacadeReadService;
import com.podo.helloprice.product.update.analysis.infra.mq.message.EmailNotifyMessage;
import com.podo.helloprice.product.update.analysis.infra.mq.message.TelegramNotifyMessage;
import com.podo.helloprice.product.update.analysis.processor.notify.Notifier;
import com.podo.helloprice.product.update.analysis.processor.notify.executor.helper.EmailContentCreator;
import com.podo.helloprice.product.update.analysis.processor.notify.executor.helper.ProductDescribe;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.podo.helloprice.core.model.PriceType.*;
import static com.podo.helloprice.core.model.ProductUpdateStatus.UPDATE_DEAD;

@RequiredArgsConstructor
@Component
public class DeadNotifyExecutor implements NotifyExecutor {

    private final Notifier notifier;
    private final NotifyTargetFacadeReadService notifyTargetFacadeReadService;

    @Override
    public ProductUpdateStatus getProductUpdateStatus() {
        return UPDATE_DEAD;
    }

    @Override
    public boolean execute(Long productId) {
        notify(productId, NORMAL);
        notify(productId, CASH);
        notify(productId, CARD);
        return true;
    }

    private void notify(Long productId, PriceType normal) {
        final NotifyTarget notifyTarget = notifyTargetFacadeReadService.get(productId, normal);
        final ProductDetailDto product = notifyTarget.getProduct();

        final String imageUrl = product.getImageUrl();
        final String title = title(product);
        final String contents = contents(product);

        for (UserDto user : notifyTarget.getUsers()) {
            notifier.notify(TelegramNotifyMessage.create(user.getTelegramId(), imageUrl, contents));
            notifier.notify(EmailNotifyMessage.create(user.getEmail(), user.getUsername(), title, EmailContentCreator.create(imageUrl, contents)));
        }
    }

    private String title(ProductDetailDto product) {
        return new StringBuilder()
                .append("'")
                .append(product.getProductName())
                .append("' 상품은 더 이상 페이지를 확인 할 수 없습니다")
                .toString();
    }

    private String contents(ProductDetailDto product) {
        return new StringBuilder()
                .append("<b>")
                .append("해당 상품의 상품페이지를 더이상 확인 할 수 없습니다.\n")
                .append("더 이상 해당 상품은 알림이 전송되지 않습니다\n")
                .append("</b>")
                .append("\n")
                .append(ProductDescribe.descProductDetailWithChangeMessage(product))
                .toString();
    }


}
