package com.xust.hotel.hosing.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageHelper;
import com.xust.hotel.common.exception.InnerErrorException;
import com.xust.hotel.hosing.service.HosingRecordService;
import com.xust.hotel.user.common.service.UserService;
import com.xust.hotel.user.common.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import per.bhj.xust.hotel_manager.dbo.HosingRecordDO;
import per.bhj.xust.hotel_manager.mapper.HosingRecordMapper;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 操作轨迹单 服务实现类
 * </p>
 *
 * @author bhj
 * @since 2021-01-03
 */
@Slf4j
@Service
public class HosingRecordServiceImpl implements HosingRecordService {

    @Resource
    private HosingRecordMapper hosingRecordMapper;

    /**
     * dubbo consumer
     */
    @Reference
    private UserService userService;

    @Override
    public List<HosingRecordDO> queryByAllNormalUser(int page, int size) throws InnerErrorException {
        try {
            List<UserVO> userVOList = userService.queryAllNormalUser();
            List<String> userList = userVOList.stream().map(UserVO::getUser).collect(Collectors.toList());;
            int count = hosingRecordMapper.queryByOperatorIn(userList).size();
            PageHelper.startPage(Math.max(page, 1), size < 1 ? 10 : size);
            List<HosingRecordDO> hosingRecordDOList = hosingRecordMapper.queryByOperatorIn(userList);
            return hosingRecordDOList;
        } catch (Exception e) {
            log.error("queryAllUser occur exception.", e);
            throw new InnerErrorException("queryAllUser occur exception.");
        }
    }
}
