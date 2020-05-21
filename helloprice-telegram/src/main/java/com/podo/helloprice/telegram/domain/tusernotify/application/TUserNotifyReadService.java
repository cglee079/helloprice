package com.podo.helloprice.telegram.domain.tusernotify.application;

import com.podo.helloprice.telegram.domain.productsale.dto.ProductSaleDto;
import com.podo.helloprice.telegram.domain.tusernotify.TUserNotify;
import com.podo.helloprice.telegram.domain.tusernotify.repository.TUserNotifyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class TUserNotifyReadService {

    private final TUserNotifyRepository userProductNotifyRepository;

    public boolean isExistedNotify(Long userId, Long productSaleId) {
        return userProductNotifyRepository.findByTUserIdAndProductSaleId(userId, productSaleId).isPresent();
    }

    public List<ProductSaleDto> findByTelegramId(String telegramId) {
        final List<TUserNotify> existedUserProductNotifies = userProductNotifyRepository.findByTelegramId(telegramId);

        return existedUserProductNotifies.stream()
                .map(notify -> new ProductSaleDto(notify.getProductSale()))
                .collect(Collectors.toList());
    }

}
