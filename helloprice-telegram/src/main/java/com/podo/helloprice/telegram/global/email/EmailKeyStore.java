package com.podo.helloprice.telegram.global.email;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;


@Component
public class EmailKeyStore {

    private Map<String, String> emailKeyStore = new HashMap<>();
    private Map<String, LocalDateTime> keyStoreTimes = new HashMap<>();
    private long cretifyTimeout = 1000 * 60 * 5;

    public String createKey(String email) {

        final long seed = System.currentTimeMillis();

        final Random rand = new Random(seed);

        final String key = String.format("%06d", rand.nextInt(999999));

        emailKeyStore.put(email, key);
        keyStoreTimes.put(key, LocalDateTime.now());

        return key;
    }

    public String certifyKey(String key, LocalDateTime now) {
        final LocalDateTime keyStoreTime = keyStoreTimes.get(key);

        if (Objects.isNull(keyStoreTime)) {
            return null;
        }

        if (now.minusMinutes(cretifyTimeout).compareTo(keyStoreTime) > 0) {
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
