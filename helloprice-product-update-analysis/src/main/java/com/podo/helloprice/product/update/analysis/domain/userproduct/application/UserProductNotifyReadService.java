package com.podo.helloprice.product.update.analysis.domain.userproduct.application;

import com.podo.helloprice.product.update.analysis.domain.userproduct.UserProductSaleNotify;
import com.podo.helloprice.product.update.analysis.domain.userproduct.repository.UserProductSaleNotifyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;


@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class UserProductNotifyReadService {

    private final UserProductSaleNotifyRepository userProductSaleNotifyRepository;

    public List<Long> findUserIdsByProductId(Long productId) {
        final List<UserProductSaleNotify> existedUserProductNotifies = userProductSaleNotifyRepository.findByProductId(productId);

        return existedUserProductNotifies.stream()
                .map(UserProductSaleNotify::getUserId)
                .collect(toList());

    }

    public List<Long> findUserIdsByProductSaleId(Long productId) {
        final List<UserProductSaleNotify> existedUserProductNotifies = userProductSaleNotifyRepository.findByProductSaleId(productId);

        return existedUserProductNotifies.stream()
                .map(UserProductSaleNotify::getUserId)
                .collect(toList());

    }
}
