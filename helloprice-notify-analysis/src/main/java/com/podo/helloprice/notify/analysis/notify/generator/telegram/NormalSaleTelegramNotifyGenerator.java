package com.podo.helloprice.notify.analysis.notify.generator.telegram;

import com.podo.helloprice.core.model.PriceType;
import com.podo.helloprice.core.model.ProductUpdateStatus;
import com.podo.helloprice.notify.analysis.notify.vo.TelegramNotify;
import com.podo.helloprice.notify.analysis.notify.contents.NotifyContents;
import com.podo.helloprice.notify.analysis.notify.generator.AbstractTelegramNotifyGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.podo.helloprice.core.model.ProductUpdateStatus.UPDATE_SALE_PRICE;

@RequiredArgsConstructor
@Component
public class NormalSaleTelegramNotifyGenerator extends AbstractTelegramNotifyGenerator {

    public static final PriceType PRICE_TYPE = PriceType.NORMAL;

    @Override
    public ProductUpdateStatus getProductUpdateStatus() {
        return UPDATE_SALE_PRICE;
    }

    @Override
    public List<TelegramNotify> generate(Long productId) {
        return generate(productId, PRICE_TYPE, NotifyContents::notifyProductSale);
    }
}
