package com.podo.helloprice.product.update.analysis.domain.tusernotify.application;

import com.podo.helloprice.product.update.analysis.domain.tusernotify.TUserNotify;
import com.podo.helloprice.product.update.analysis.domain.tusernotify.repository.TUserNotifyRepository;
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
public class TUserNotifyReadService {

    private final TUserNotifyRepository TUserNotifyRepository;

    public List<Long> findUserIdsByProductId(Long productId) {
        final List<TUserNotify> existedUserProductNotifies = TUserNotifyRepository.findByProductId(productId);

        return existedUserProductNotifies.stream()
                .map(TUserNotify::getTuser_id)
                .collect(toList());

    }

    public List<Long> findUserIdsByProductSaleId(Long productId) {
        final List<TUserNotify> existedUserProductNotifies = TUserNotifyRepository.findByProductSaleId(productId);

        return existedUserProductNotifies.stream()
                .map(TUserNotify::getTuser_id)
                .collect(toList());

    }
}
