package com.podo.helloprice.product.update.analysis.domain.tusernotify.application;

import com.podo.helloprice.product.update.analysis.domain.tusernotify.TUserNotify;
import com.podo.helloprice.product.update.analysis.domain.tusernotify.repository.TUserNotifyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class TUserNotifyDeleteService {

    private final TUserNotifyRepository TUserNotifyRepository;

    public void deleteNotifiesByProductId(Long productId) {
        final List<TUserNotify> userProductNotifies = TUserNotifyRepository.findByProductId(productId);

        for (TUserNotify TUserNotify : userProductNotifies) {
            this.TUserNotifyRepository.delete(TUserNotify);
        }
    }

}
