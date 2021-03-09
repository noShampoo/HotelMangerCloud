package com.xust.hotel.common.security;

import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;

/**
 * @author bhj
 */
public class CryptUtil {

    private static final int RADIX = 16;

    private static final String SEED = "0933910847463829232312312";

    /**
     * 加密
     * @param sourceStr source string
     * @return string
     */
    public static String encrypt(String sourceStr) {
        if (StringUtils.isBlank(sourceStr)) {
            return null;
        }
        BigInteger bi_passwd = new BigInteger(sourceStr.getBytes());
        BigInteger bi_r0 = new BigInteger(SEED);
        BigInteger bi_r1 = bi_r0.xor(bi_passwd);
        return bi_r1.toString(RADIX);
    }

    /**
     * 解密
     * @param encrypted source string
     * @return string
     */
    public static String decrypt(String encrypted) {
        if (StringUtils.isBlank(encrypted)) {
            return null;
        }
        BigInteger bi_confuse = new BigInteger(SEED);
        try {
            BigInteger bi_r1 = new BigInteger(encrypted, RADIX);
            BigInteger bi_r0 = bi_r1.xor(bi_confuse);
            return new String(bi_r0.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
