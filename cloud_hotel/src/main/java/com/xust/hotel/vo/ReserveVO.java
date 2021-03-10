package com.xust.hotel.vo;

import com.alibaba.fastjson.JSONObject;
import com.xust.hotel.common.dto.BasePojo;
import lombok.*;
import per.bhj.xust.hotel_manager.dbo.CustomerInfoPojo;

import java.util.List;

/**
 * @author bhj
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ReserveVO extends BasePojo {

    private static final long serialVersionUID = 8649713448177974146L;
    /**
     * 预定房间号
     */
    private String roomNo;

    /**
     * 预定时长
     */
    private Integer reserveDay;

    /**
     * 预定时间段
     */
    private String reserveTime;

    /**
     * customer info
     */
    private List<CustomerInfoPojo> customerInfo;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * rt0->已预定,rt1->已消费,rt2->已取消
     */
    private String reserveStatus;

    /**
     * 当前操作人登录账号
     */
    private String operateCp;

    /**
     * 操作时间
     */
    private String operateTime;

    /**
     * count
     * @return
     */
    private Integer count;


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
