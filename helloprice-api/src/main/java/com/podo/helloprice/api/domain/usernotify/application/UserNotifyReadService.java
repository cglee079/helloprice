package com.podo.helloprice.api.domain.usernotify.application;

import com.podo.helloprice.api.domain.productsale.ProductSale;
import com.podo.helloprice.api.domain.productsale.repository.ProductSaleRepository;
import com.podo.helloprice.api.domain.usernotify.dto.UserNotifyResponseDto;
import com.podo.helloprice.api.domain.usernotify.model.UserNotify;
import com.podo.helloprice.api.domain.usernotify.repository.UserNotifyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserNotifyReadService {

    private final UserNotifyRepository userNotifyRepository;
    private final ProductSaleRepository productSaleRepository;

    public List<UserNotifyResponseDto> list(Long userId){

        final List<UserNotify> userNotifies = userNotifyRepository.findByUserId(userId);

        final List<Long> productSaleIds = userNotifies.stream()
                .map(UserNotify::getProductSaleId)
                .collect(Collectors.toList());

        final Map<Long, ProductSale> idToProductSale = productSaleRepository.findByIdIn(productSaleIds).stream()
                .collect(Collectors.toMap(ProductSale::getId, ps -> ps));

        return userNotifies.stream()
                .map(userNotify -> new UserNotifyResponseDto(userNotify.getId(), idToProductSale.get(userNotify.getProductSaleId())))
                .collect(Collectors.toList());

    }

}
