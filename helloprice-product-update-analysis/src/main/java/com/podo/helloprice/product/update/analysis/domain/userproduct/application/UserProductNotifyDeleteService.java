package com.podo.helloprice.product.update.analysis.domain.userproduct.application;

import com.podo.helloprice.product.update.analysis.domain.userproduct.UserProductNotify;
import com.podo.helloprice.product.update.analysis.domain.userproduct.repository.UserProductNotifyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserProductNotifyDeleteService {

    private final UserProductNotifyRepository userProductNotifyRepository;

    public void deleteNotifiesByProductId(Long productId) {
        final List<UserProductNotify> userProductNotifies = userProductNotifyRepository.findByProductId(productId);

        for (UserProductNotify userProductNotify : userProductNotifies) {
            this.userProductNotifyRepository.delete(userProductNotify);
        }
    }

}
