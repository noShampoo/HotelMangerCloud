package com.xust.hotel.hosing.service;

import com.xust.hotel.common.exception.*;
import com.xust.hotel.vo.ReserveVO;

import java.util.List;

/**
 * <p>
 * 预定信息单 服务类
 * </p>
 *
 * @author bhj
 * @since 2021-01-03
 */
public interface ReserveRoomInfoService {

    /**
     * reserve
     * @param data
     * @return
     */
    String reserveRoom(ReserveVO data) throws CustomerInfoException, CanNotReserveException, AccessException, NoSuchKeyException, InnerErrorException, MapperErrorException;


    /**
     * chancel
     * @param data
     * @return
     */
    public boolean chancelRoom(ReserveVO data) throws NoSuchFieldException, ChancelReserveErrorException, BizInfoErrorException, MapperErrorException, InnerErrorException;

    /**
     * query
     * @param page
     * @param size
     * @return
     */
    public List<ReserveVO> queryAll(int page, int size, String status) throws InnerErrorException;

    /**
     * query some
     * @param paramType
     * @param param
     * @return
     * @throws InnerErrorException
     */
    public ReserveVO queryDynamic(String paramType, String param) throws InnerErrorException;
}
