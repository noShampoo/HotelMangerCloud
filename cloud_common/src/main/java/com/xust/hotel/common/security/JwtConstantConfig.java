package com.xust.hotel.common.security;

/**
 * @author bhj
 */
public class JwtConstantConfig {

    public static final String HEADER_KEY = "Authorization";

    public static final String HEADER_TOKEN_START = "bWy7Gz2SXouST2";

    public static final String HEADER_TOKEN_END = "bwyGZSzwYbsg";

    public static final int SUB_START_NUM = 14;

    public static final int SUB_END_NUM = 12;

    public static final String USER_ROLE_ADMIN = "admin";

    public static final String USER_ROLE_NORMAL = "normal";

    public static final String REQ_ATTRIBUTE_KEY = "login-info";

    public static final String REGISTER_ADMIN_HEADER_KEY = "Admin7202";

    public static final String REGISTER_ADMIN_HEADER_VALUE = HEADER_TOKEN_START + HEADER_TOKEN_END;

}
