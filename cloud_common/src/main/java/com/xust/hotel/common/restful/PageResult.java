package com.xust.hotel.common.restful;

import com.alibaba.fastjson.JSONObject;
import com.xust.hotel.common.dto.BasePojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author bhj
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PageResult<T> extends BasePojo {
    private static final long serialVersionUID = -694738051259047931L;

    /**
     * 总数
     */
    private Long total;

    /**
     * 数据
     */
    private List<T> row;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
