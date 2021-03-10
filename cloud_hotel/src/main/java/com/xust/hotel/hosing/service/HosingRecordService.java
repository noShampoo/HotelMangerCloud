package com.xust.hotel.hosing.service;

import com.xust.hotel.common.exception.InnerErrorException;
import per.bhj.xust.hotel_manager.dbo.HosingRecordDO;

import java.util.List;

/**
 * <p>
 * 操作轨迹单 服务类
 * </p>
 *
 * @author bhj
 * @since 2021-01-03
 */
public interface HosingRecordService {

    /**
     * 查询所有普通用户操作的记录
     * @param page
     * @param size
     * @return
     */
    List<HosingRecordDO> queryByAllNormalUser(int page, int size) throws InnerErrorException;

}
