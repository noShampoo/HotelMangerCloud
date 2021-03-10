package com.xust.hotel.hosing.service;

import com.xust.hotel.common.exception.*;
import com.xust.hotel.vo.GuestRoomVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 客房单 服务类
 * </p>
 *
 * @author bhj
 * @since 2021-01-03
 */
public interface GuestRoomService {

    /**
     * add
     * @param roomNo
     * @param roomDetail
     * @param roomStatus
     * @return
     */
    boolean add(String roomNo, String roomDetail, String roomStatus) throws StatusErrorException, KeyExistException, InnerErrorException, NoSuchKeyException;

    /**
     * modify
     * @param roomNo
     * @param roomDetail
     * @return
     */
    boolean modify(String roomNo, String roomDetail) throws NoSuchKeyException, NotChangeException, InnerErrorException;

    /**
     * delete
     * @param roomNo
     * @return
     */
    boolean delete(String roomNo) throws NoSuchKeyException, KeyExistException, StatusErrorException, InnerErrorException, NotChangeException;

    /**
     * query all
     * @param page
     * @param size
     * @return
     */
    List<GuestRoomVO> queryAll(int page, int size) throws InnerErrorException;

    /**
     * query some
     * @param roomNo
     * @return
     */
    GuestRoomVO querySome(String roomNo) throws InnerErrorException;

    /**
     * query by roomKey
     */
    List<GuestRoomVO> queryByRoomKey(String roomKey, int page, int size) throws InnerErrorException;

    /**
     * query count
     * @return
     */
    Map<String, Integer> queryCount() throws InnerErrorException;
}
