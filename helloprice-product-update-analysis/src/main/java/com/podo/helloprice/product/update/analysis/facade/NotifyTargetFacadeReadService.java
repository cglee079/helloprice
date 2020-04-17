package com.podo.helloprice.product.update.analysis.facade;

import com.podo.helloprice.core.model.PriceType;
import com.podo.helloprice.core.model.UserStatus;
import com.podo.helloprice.product.update.analysis.domain.product.application.ProductReadService;
import com.podo.helloprice.product.update.analysis.domain.product.dto.ProductDetailDto;
import com.podo.helloprice.product.update.analysis.domain.user.UserReadService;
import com.podo.helloprice.product.update.analysis.domain.user.dto.UserDto;
import com.podo.helloprice.product.update.analysis.domain.userproduct.application.UserProductNotifyReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class NotifyTargetFacadeReadService {

    private final ProductReadService productReadService;
    private final UserProductNotifyReadService userProductNotifyReadService;
    private final UserReadService userReadService;

    public NotifyTarget get(Long productId, PriceType priceType){
        final ProductDetailDto product = productReadService.findByProductId(productId, priceType);
        final List<Long> userIds = userProductNotifyReadService.findUserIdsByProductIdAndPriceType(productId, priceType);
        final List<UserDto> users = userReadService.findByUserIdsAndUserStatus(userIds, UserStatus.ALIVE);

        return new NotifyTarget(users, product);
    }

}
