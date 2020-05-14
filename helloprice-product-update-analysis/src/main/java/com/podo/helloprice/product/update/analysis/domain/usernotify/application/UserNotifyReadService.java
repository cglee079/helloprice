package com.podo.helloprice.product.update.analysis.domain.usernotify.application;

import com.podo.helloprice.product.update.analysis.domain.tusernotify.TUserNotify;
import com.podo.helloprice.product.update.analysis.domain.usernotify.model.UserNotify;
import com.podo.helloprice.product.update.analysis.domain.usernotify.repository.UserNotifyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;


@Transactional
@RequiredArgsConstructor
@Service
public class UserNotifyReadService {

    private final UserNotifyRepository userNotifyRepository;

    public List<Long> findUserIdsByProductId(Long productId) {
        final List<UserNotify> existedUserProductNotifies = userNotifyRepository.findByProductId(productId);

        return existedUserProductNotifies.stream()
                .distinct()
                .map(UserNotify::getUserId)
                .collect(toList());


    }

    public List<Long> findUserIdsByProductSaleId(Long productSaleId) {
        return null;
    }
}
