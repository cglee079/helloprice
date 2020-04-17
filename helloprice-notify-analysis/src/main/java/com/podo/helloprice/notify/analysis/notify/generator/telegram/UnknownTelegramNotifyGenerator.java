package com.podo.helloprice.notify.analysis.notify.generator.telegram;

import com.podo.helloprice.core.model.ProductUpdateStatus;
import com.podo.helloprice.notify.analysis.domain.product.dto.ProductDetailDto;
import com.podo.helloprice.notify.analysis.notify.vo.TelegramNotify;
import com.podo.helloprice.notify.analysis.notify.contents.NotifyContents;
import com.podo.helloprice.notify.analysis.notify.generator.AbstractTelegramNotifyGenerator;
import com.podo.helloprice.core.model.PriceType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor
@Component
public class UnknownTelegramNotifyGenerator extends AbstractTelegramNotifyGenerator {

    private static final Function<ProductDetailDto, String> CONTENTS = NotifyContents::notifyProductUnknown;

    @Override
    public ProductUpdateStatus getProductUpdateStatus() {
        return ProductUpdateStatus.UPDATE_UNKNOWN;
    }

    @Override
    public List<TelegramNotify> generate(Long productId) {
        final List<TelegramNotify> telegramNotifies = new ArrayList<>();

        telegramNotifies.addAll(this.generate(productId, PriceType.NORMAL, CONTENTS));
        telegramNotifies.addAll(this.generate(productId, PriceType.CASH, CONTENTS));
        telegramNotifies.addAll(this.generate(productId, PriceType.CARD, CONTENTS));

        return telegramNotifies;
    }


}
