package com.xust.hotel.vo;

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
public class RoomDetailVO extends BasePojo {
    private static final long serialVersionUID = -2594212882058637436L;

    /**
     * room type key
     */
    private String roomKey;

    /**
     * room type
     */
    private String roomType;

    /**
     * room price
     */
    private Double roomPrice;

    /**
     * price unit
     */
    private String priceUnit;

    /**
     * count
     */
    private int count;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
