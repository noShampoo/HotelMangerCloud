package com.xust.hotel.common.dto;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * base dto
 *
 * @author bhj
 */
public class BasePojo implements Serializable {

    private static final long serialVersionUID = -2600956692209503971L;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
