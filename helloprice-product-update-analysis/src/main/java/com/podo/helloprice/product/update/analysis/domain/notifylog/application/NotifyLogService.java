package com.podo.helloprice.product.update.analysis.domain.notifylog.application;

import com.podo.helloprice.product.update.analysis.domain.notifylog.repository.NotifyLogRepository;
import com.podo.helloprice.product.update.analysis.domain.notifylog.dto.NotifyLogDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class NotifyLogService {

    private final NotifyLogRepository notifyLogRepository;

    public void insertNewNotifyLog(NotifyLogDto notifyLog) {
        notifyLogRepository.save(notifyLog.toEntity());
    }
}
