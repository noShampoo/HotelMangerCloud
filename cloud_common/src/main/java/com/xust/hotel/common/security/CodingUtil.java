package com.xust.hotel.common.security;

import java.util.Random;

/**
 * @author bhj
 */
public class CodingUtil {

    private static final String USER_START = "7202-";

    private static final String ORDER_NO_START = "XUST7202-";

    private static final String SPLIT_CHAR = "-";


    /**
     * 生成订单编号
     * @return order_no
     */
    public static String generateOrderNo() {
        String randomStr = String.valueOf((int) ((Math.random() * 9 + 1) * 10000));
        String nowTime = String.valueOf(System.currentTimeMillis() / 10);
        return ORDER_NO_START + randomStr + nowTime;
    }

    /**
     * 生成user
     * @param name
     * @return
     */
    public static String generateUser(String name) {
        String random = String.valueOf((int) ((Math.random() * 9 + 1) * 10000));
        return USER_START + name + random;
    }



}
