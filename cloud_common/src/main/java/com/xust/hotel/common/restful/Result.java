package com.xust.hotel.common.restful;

import com.alibaba.fastjson.JSONObject;
import com.xust.hotel.common.dto.BasePojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author bhj
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Result extends BasePojo {
    private static final long serialVersionUID = -8514365824579404364L;

    /**
     * 是否成功
     */
    private boolean flag;

    /**
     * 返回状态
     */
    private StatusEnum statusEnum;

    /**
     * 附加信息
     */
    private String attachMsg;

    /**
     * 返回数据
     */
    private Object data;

    public boolean isSuccess() {
        return this.flag;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
