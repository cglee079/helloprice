package com.podo.helloprice.telegram.global.email;

import com.podo.helloprice.core.util.MyNumberUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Slf4j
@Component
public class EmailKeyStore {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);

    private Map<String, String> emailKeyStore = new ConcurrentHashMap<>();
    private Map<String, LocalDateTime> keyStoreTimes = new ConcurrentHashMap<>();

    @Value("${email.key.auth_timeout}")
    private long certifyTimeout;

    public String createKey(String email) {
        final String key = getKey();

        emailKeyStore.put(email, key);
        keyStoreTimes.put(key, LocalDateTime.now());

        scheduleRemoveExpire(email, key);

        return key;
    }

    private void scheduleRemoveExpire(String email, String key) {
        scheduler.schedule(() -> {
            log.info("{}({}) 인증시간이 만료되어 삭제합니다", email, key);
            this.emailKeyStore.remove(email);
            this.keyStoreTimes.remove(key);
        }, certifyTimeout, TimeUnit.MINUTES);
    }

    private String getKey() {
        final String key = String.format("%06d", MyNumberUtils.rand(999999));

        //이미 적재된 동일한 키가 있는 경우
        if (keyStoreTimes.containsKey(key)) {
            return getKey();
        }

        return key;
    }

    public String certifyKey(String key, LocalDateTime now) {
        final LocalDateTime keyStoreTime = keyStoreTimes.get(key);

        if (Objects.isNull(keyStoreTime)) {
            log.info("저장되어있지 않은 KEY 입니다 {}", key);
            return null;
        }

        if (now.minusMinutes(certifyTimeout).compareTo(keyStoreTime) > 0) {
            log.info("시간이 만료된 KEY 입니다 {}", key);
            return null;
        }

        final String email = getEmailByKey(key);

        emailKeyStore.remove(email);
        keyStoreTimes.remove(key);

        return email;
    }

    private String getEmailByKey(String key) {
        for (String email : emailKeyStore.keySet()) {
            if (emailKeyStore.get(email).equals(key)) {
                return email;
            }
        }

        return null;
    }


}
