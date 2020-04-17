package com.podo.helloprice.telegram.domain.userproduct.application;

import com.podo.helloprice.core.model.PriceType;
import com.podo.helloprice.telegram.domain.product.dto.ProductDetailDto;
import com.podo.helloprice.telegram.domain.userproduct.UserProductNotify;
import com.podo.helloprice.telegram.domain.userproduct.repository.UserProductNotifyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class UserProductNotifyReadService {

    private final UserProductNotifyRepository userProductNotifyRepository;

    public boolean isExistedNotify(Long userId, Long productId, PriceType priceType) {
        final UserProductNotify existedNotify = userProductNotifyRepository.findByUserIdAndProductIdAndPriceType(userId, productId, priceType);
        return Objects.nonNull(existedNotify);
    }

    public List<ProductDetailDto> findNotifyProductsByUserTelegramId(String telegramId) {
        final List<UserProductNotify> existedUserProductNotifies = userProductNotifyRepository.findByUserTelegramId(telegramId);

        return existedUserProductNotifies.stream()
                .map(notify -> new ProductDetailDto(notify.getProduct(), notify.getPriceType()))
                .collect(Collectors.toList());
    }

}
