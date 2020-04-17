package com.podo.helloprice.notify.analysis.domain.userproduct.application;

import com.podo.helloprice.core.model.PriceType;
import com.podo.helloprice.notify.analysis.domain.userproduct.UserProductNotify;
import com.podo.helloprice.notify.analysis.domain.userproduct.repository.UserProductNotifyRepository;
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

    private final UserProductNotifyRepository userProductNotifyRepository;

    public List<Long> findUserIdsByProductIdAndPriceType(Long productId, PriceType priceType) {
        final List<UserProductNotify> existedUserProductNotifies = userProductNotifyRepository.findByProductIdAndPriceType(productId, priceType);

        return existedUserProductNotifies.stream()
                .map(UserProductNotify::getUserId)
                .collect(toList());
    }

}
