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
public class GuestRoomVO extends BasePojo {
    private static final long serialVersionUID = 2613157886893035086L;

    /**
     * room no
     */
    private String roomNo;

    /**
     * room status
     */
    private String roomStatus;

    /**
     * room detail
     */
    private String roomDetail;

    /**
     * order no
     */
    private String orderNo;

    /**
     * count
     */
    private Integer count;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
