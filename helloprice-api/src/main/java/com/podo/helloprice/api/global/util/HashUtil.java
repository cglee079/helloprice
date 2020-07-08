package com.podo.helloprice.api.global.util;

import com.podo.helloprice.api.global.util.exception.HashFailException;
import lombok.experimental.UtilityClass;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@UtilityClass
public class HashUtil {

    private static final String JOIN_DELIMITER = ":";

    public static String hash(String... values) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(String.join(JOIN_DELIMITER, values).getBytes());
            return bytesToHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new HashFailException(e);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }


}
