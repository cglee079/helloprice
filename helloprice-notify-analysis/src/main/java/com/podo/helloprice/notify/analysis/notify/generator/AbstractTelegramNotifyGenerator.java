package com.podo.helloprice.notify.analysis.notify.generator;

import com.podo.helloprice.core.model.PriceType;
import com.podo.helloprice.core.model.UserStatus;
import com.podo.helloprice.notify.analysis.domain.product.application.ProductReadService;
import com.podo.helloprice.notify.analysis.domain.product.dto.ProductDetailDto;
import com.podo.helloprice.notify.analysis.domain.user.UserReadService;
import com.podo.helloprice.notify.analysis.domain.userproduct.application.UserProductNotifyReadService;
import com.podo.helloprice.notify.analysis.notify.vo.TelegramNotify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;


@Component
public abstract class AbstractTelegramNotifyGenerator implements TelegramNotifyGenerator{

    @Autowired
    private ProductReadService productReadService;

    @Autowired
    private UserProductNotifyReadService userProductNotifyReadService;

    @Autowired
    private UserReadService userReadService;

    protected List<TelegramNotify> generate(Long productId, PriceType priceType, Function<ProductDetailDto, String> contents) {
        final ProductDetailDto product = productReadService.findByProductId(productId, priceType);
        final List<Long> userIds = userProductNotifyReadService.findUserIdsByProductIdAndPriceType(productId, priceType);
        final List<String> telegramIds = userReadService.findTelegramIdsByUserIdsAndUserStatus(userIds, UserStatus.ALIVE);

        return telegramIds.stream()
                .map(telegramId -> new TelegramNotify(telegramId, contents.apply(product)))
                .collect(toList());
    }

}
