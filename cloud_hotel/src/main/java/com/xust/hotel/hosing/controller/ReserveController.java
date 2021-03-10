package com.xust.hotel.hosing.controller;

import com.xust.hotel.common.constantAndMapper.UniversalConstant;
import com.xust.hotel.common.exception.*;
import com.xust.hotel.common.restful.RequestParam;
import com.xust.hotel.common.restful.Result;
import com.xust.hotel.common.restful.StatusEnum;
import com.xust.hotel.common.security.AccessUtil;
import com.xust.hotel.common.security.JwtConstantConfig;
import com.xust.hotel.hosing.service.ReserveRoomInfoService;
import com.xust.hotel.vo.ReserveVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author bhj
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = "/reserve")
public class ReserveController {

    @Autowired
    private ReserveRoomInfoService reserveRoomInfoService;

    @PostMapping("/room")
    public Result reserveRoom(@RequestBody RequestParam<ReserveVO> requestParam,
                              HttpServletRequest request) throws InnerErrorException, NoSuchKeyException, CanNotReserveException, CustomerInfoException, AccessException, MapperErrorException {
        if (requestParam == null || requestParam.getData() == null) {
            log.error("reserveRoom, param error.");
            return new Result(true, StatusEnum.PARAM_ERROR, "param error", null);
        }
        if (!AccessUtil.checkAccess(request, JwtConstantConfig.USER_ROLE_ADMIN,
                JwtConstantConfig.USER_ROLE_NORMAL)) {
            log.error("reserveRoom, access error.");
            return new Result(true, StatusEnum.ACCESS_ERROR, null, null);
        }
        ReserveVO data = requestParam.getData();
        if (StringUtils.isBlank(data.getRoomNo()) || data.getReserveDay() == null || data.getReserveDay() < 1
            || StringUtils.isBlank(data.getReserveTime()) || CollectionUtils.isEmpty(data.getCustomerInfo())) {
            log.error("reserveRoom, param error.data={}", data.toString());
            return new Result(true, StatusEnum.PARAM_ERROR, "param's data={}" + data.toString(), null);
        }
        String orderNO = reserveRoomInfoService.reserveRoom(data);
        if (StringUtils.isNotBlank(orderNO)) {
            return new Result(true, StatusEnum.OK, null, orderNO);
        }
        log.error("reserveRoom, service reserve error.");
        return new Result(true, StatusEnum.ERROR, null, null);
    }

    @PostMapping("/chancel")
    public Result chancelRoom(@RequestBody RequestParam<ReserveVO> requestParam,
                              HttpServletRequest request) throws InnerErrorException, ChancelReserveErrorException, MapperErrorException, BizInfoErrorException, NoSuchFieldException {
        if (requestParam == null || requestParam.getData() == null) {
            log.error("reserveRoom, param error.");
            return new Result(true, StatusEnum.PARAM_ERROR, "param error", null);
        }
        if (!AccessUtil.checkAccess(request, JwtConstantConfig.USER_ROLE_ADMIN,
                JwtConstantConfig.USER_ROLE_NORMAL)) {
            log.error("reserveRoom, access error.");
            return new Result(true, StatusEnum.ACCESS_ERROR, null, null);
        }
        ReserveVO data = requestParam.getData();
        if (StringUtils.isBlank(data.getRoomNo()) || StringUtils.isBlank(data.getOrderNo())) {
            log.error("reserveRoom, param error.data={}", data.toString());
            return new Result(true, StatusEnum.PARAM_ERROR, "param's data={}" + data.toString(), null);
        }
        if (reserveRoomInfoService.chancelRoom(data)) {
            return new Result(true, StatusEnum.OK, null, null);
        }
        log.error("reserveRoom, service reserve error.");
        return new Result(true, StatusEnum.ERROR, null, null);
    }


    @GetMapping("/all/{page}/{size}")
    public Result queryAll(@PathVariable("page")int page,
                           @PathVariable("size")int size,
                           HttpServletRequest request) throws InnerErrorException {
        if (page < 0 || size <= 0) {
            log.error("queryAll, param error.");
            return new Result(true, StatusEnum.PARAM_ERROR, null, null);
        }
        if (!AccessUtil.checkAccess(request, JwtConstantConfig.USER_ROLE_ADMIN,
                JwtConstantConfig.USER_ROLE_NORMAL)) {
            log.error("queryAll, access error.");
            return new Result(true, StatusEnum.ACCESS_ERROR, null, null);
        }
        List<ReserveVO> reserveVOS = reserveRoomInfoService.queryAll(page, size, UniversalConstant.ROOM_OPERATE_STATUS_RESERVE);
        return new Result(true, StatusEnum.OK, null, reserveVOS);
    }

    @GetMapping("/{paramType}/{param}")
    public Result querySome(@PathVariable("paramType")String paramType,
                            @PathVariable("param")String param,
                            HttpServletRequest request) throws InnerErrorException {
        if (StringUtils.isBlank(paramType) || StringUtils.isBlank(param)) {
            log.error("querySome, param error");
            return new Result(true, StatusEnum.PARAM_ERROR, null, null);
        }
        if (!AccessUtil.checkAccess(request, JwtConstantConfig.USER_ROLE_ADMIN,
                JwtConstantConfig.USER_ROLE_NORMAL)) {
            log.error("queryAll, access error.");
            return new Result(true, StatusEnum.ACCESS_ERROR, null, null);
        }
        if (!paramType.equals(UniversalConstant.RESERVE_INFO_QUERY_PARAM_TYPE_ORDER_NO)
                && !paramType.equals(UniversalConstant.RESERVE_INFO_QUERY_PARAM_TYPE_ROOM_NO)) {
            log.error("querySome, paramType error.paramType={}, param={}", paramType, param);
            return new Result(true, StatusEnum.PARAM_ERROR, null, null);
        }
        ReserveVO reserveVO = reserveRoomInfoService.queryDynamic(paramType, param);
        return new Result(true, StatusEnum.OK, null, reserveVO);
    }

}
