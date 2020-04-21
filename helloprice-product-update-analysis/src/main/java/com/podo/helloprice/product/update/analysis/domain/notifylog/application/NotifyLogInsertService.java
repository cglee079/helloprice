package com.podo.helloprice.product.update.analysis.domain.notifylog.application;

import com.podo.helloprice.product.update.analysis.domain.notifylog.repository.NotifyLogRepository;
import com.podo.helloprice.product.update.analysis.domain.notifylog.dto.NotifyLogInsertDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class NotifyLogInsertService {

    private final NotifyLogRepository notifyLogRepository;

    public void insertNew(NotifyLogInsertDto notifyLog) {
        notifyLogRepository.save(notifyLog.toEntity());
    }
}
