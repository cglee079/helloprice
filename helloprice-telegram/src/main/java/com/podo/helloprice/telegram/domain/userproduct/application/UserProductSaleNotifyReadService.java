package com.podo.helloprice.telegram.domain.userproduct.application;

import com.podo.helloprice.telegram.domain.productsale.dto.ProductSaleDto;
import com.podo.helloprice.telegram.domain.userproduct.UserProductSaleNotify;
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
public class UserProductSaleNotifyReadService {

    private final UserProductNotifyRepository userProductNotifyRepository;

    public boolean isExistedNotify(Long userId, Long productSaleId) {
        final UserProductSaleNotify existedNotify = userProductNotifyRepository.findByUserIdAndProductSaleId(userId, productSaleId);
        return Objects.nonNull(existedNotify);
    }

    public List<ProductSaleDto> findByTelegramId(String telegramId) {
        final List<UserProductSaleNotify> existedUserProductNotifies = userProductNotifyRepository.findByTelegramId(telegramId);

        return existedUserProductNotifies.stream()
                .map(notify -> new ProductSaleDto(notify.getProductSale()))
                .collect(Collectors.toList());
    }

}
