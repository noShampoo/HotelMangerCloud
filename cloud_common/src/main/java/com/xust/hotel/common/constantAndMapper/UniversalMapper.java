package com.xust.hotel.common.constantAndMapper;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * @author bhj
 */
public class UniversalMapper {

    public static final Map<String, String> USER_TYPE_MAPPER = ImmutableMap
            .<String, String>builder()
            .put("normal", UniversalConstant.USER_TABLE_TYPE_NORMAL)
            .put("admin", UniversalConstant.USER_TABLE_TYPE_ADMIN)
            .build();
}
