package com.xust.hotel.user.common.vo;

import com.alibaba.fastjson.JSONObject;
import com.xust.hotel.common.dto.BasePojo;
import lombok.*;

/**
 * @author bhj
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserVO extends BasePojo {
    private static final long serialVersionUID = 686897910642818110L;

    /**
     * user
     */
    private String user;

    /**
     * name
     */
    private String name;

    /**
     * type
     */
    private String type;

    /**
     * count
     */
    private Integer count;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
