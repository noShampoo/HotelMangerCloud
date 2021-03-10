package com.xust.hotel.hosing.service.impl;

import com.github.pagehelper.PageHelper;
import com.google.common.collect.Maps;
import com.xust.hotel.common.constantAndMapper.UniversalConstant;
import com.xust.hotel.common.exception.*;
import com.xust.hotel.hosing.service.GuestRoomService;
import com.xust.hotel.vo.GuestRoomVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import per.bhj.xust.hotel_manager.dbo.GuestRoomDO;
import per.bhj.xust.hotel_manager.dbo.RoomInfoDO;
import per.bhj.xust.hotel_manager.mapper.GuestRoomMapper;
import per.bhj.xust.hotel_manager.mapper.RoomInfoMapper;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 客房单 服务实现类
 * </p>
 *
 * @author bhj
 * @since 2021-01-03
 */
@Slf4j
@Service
public class GuestRoomServiceImpl implements GuestRoomService {

    @Resource
    private GuestRoomMapper guestRoomMapper;

    @Resource
    private RoomInfoMapper roomInfoMapper;

    @Override
    public boolean add(String roomNo, String roomDetail, String roomStatus) throws StatusErrorException, KeyExistException, InnerErrorException, NoSuchKeyException {
        try {
            log.info("add, roomNo={}, roomDetail={}, roomStatus={}", roomNo, roomDetail
                    , roomStatus);
            if (StringUtils.isBlank(roomNo) || StringUtils.isBlank(roomDetail) || StringUtils.isBlank(roomStatus)) {
                log.error("add, param error.roomNo={}, roomDetail={}, roomStatus={}", roomNo, roomDetail
                        , roomStatus);
                return false;
            }
            if (!roomStatus.equals(UniversalConstant.GUEST_ROOM_TABLE_R_STATUS_NO_PERSON)
                    && !roomStatus.equals(UniversalConstant.GUEST_ROOM_TABLE_R_STATUS_PERSON)
                    && !roomStatus.equals(UniversalConstant.GUEST_ROOM_TABLE_R_STATUS_RESERVE)) {
                log.error("add, param error.roomNo={}, roomDetail={}, roomStatus={}", roomNo, roomDetail
                        , roomStatus);
                throw new StatusErrorException("status error.");
            }
            RoomInfoDO roomInfoDO = roomInfoMapper.queryByRoomTypeKey(roomDetail);
            GuestRoomDO guestRoomDO = guestRoomMapper.queryByRoomNo(roomNo);
            if (roomInfoDO == null) {
                log.error("add, this type exist.roomDetail={}", roomDetail);
                throw new NoSuchKeyException("roomDetail didn't exist.");
            }
            if (guestRoomDO != null) {
                log.error("add, this room no exist.guestRoomDO={}", guestRoomDO.toString());
                throw new KeyExistException("room no exist");
            }
            if (guestRoomMapper.insertDynamic(GuestRoomDO.builder()
                    .roomNo(roomNo)
                    .roomDetail(roomDetail)
                    .roomStatus(roomStatus)
                    .phyStatus(UniversalConstant.GUEST_ROOM_TABLE_PHY_STATUS_USING)
                    .build()
            )) {
                return true;
            }
            log.error("add, mapper insert error.");
            return false;
        } catch (NoSuchKeyException e) {
            log.error("add, roomDetail didn't exist.", e);
            throw new NoSuchKeyException("roomDetail exist.");
        } catch (KeyExistException e) {
            log.error("add, room no exist.", e);
            throw new KeyExistException("room no exist");
        } catch (StatusErrorException e) {
            log.error("add, status type error.", e);
            throw new StatusErrorException("status type error.");
        } catch (Exception e) {
            log.error("add occur exception.", e);
            throw new InnerErrorException("add occur exception.");
        }
    }

    @Override
    public boolean modify(String roomNo, String roomDetail) throws NoSuchKeyException, NotChangeException, InnerErrorException {
        try {
            log.info("modify, roomNo={}, roomDetail={}", roomNo, roomDetail);
            if (StringUtils.isBlank(roomNo) || StringUtils.isBlank(roomDetail)) {
                log.error("modify, param error.roomNo={}, roomDetail={}", roomNo, roomDetail);
                return false;
            }
            RoomInfoDO roomInfoDO = roomInfoMapper.queryByRoomTypeKey(roomDetail);
            GuestRoomDO guestRoomDO = guestRoomMapper.queryByRoomNo(roomNo);
            if (roomInfoDO == null) {
                log.error("modify, no such roomDetail..roomDetail={}", roomDetail);
                throw new NoSuchKeyException("no such roomDetail.");
            }
            if (guestRoomDO == null) {
                log.error("modify, no such roomNo.roomNo={}", roomNo);
                throw new NoSuchKeyException("no such roomNo.");
            }
            if (!guestRoomDO.getRoomStatus().equals(UniversalConstant.GUEST_ROOM_TABLE_R_STATUS_NO_PERSON)) {
                log.error("modify, this room hosing.roomNo={}", roomNo);
                throw new NotChangeException("this room hosing.");
            }
            if (guestRoomMapper.updateRoomDetailByRoomNo(roomNo, roomDetail)) {
                return true;
            }
            log.error("modify, mapper insert error.");
            return false;
        } catch (NoSuchKeyException e) {
            log.error("modify, no such roomDetail.", e);
            throw new NoSuchKeyException("no such roomDetail.");
        } catch (NotChangeException e) {
            log.error("modify, this room hosing.", e);
            throw new NotChangeException("this room hosing");
        } catch (Exception e) {
            log.error("modify occur exception.", e);
            throw new InnerErrorException("add occur exception.");
        }
    }

    @Override
    public boolean delete(String roomNo) throws NoSuchKeyException, KeyExistException, StatusErrorException, InnerErrorException, NotChangeException {
        try {
            log.info("delete, roomNo={}", roomNo);
            if (StringUtils.isBlank(roomNo)) {
                log.error("delete, param error.roomNo={}", roomNo);
                return false;
            }
            GuestRoomDO guestRoomDO = guestRoomMapper.queryByRoomNo(roomNo);
            if (guestRoomDO == null) {
                log.error("delete, no such roomNo.roomNo={}", roomNo);
                throw new NoSuchKeyException("no such roomNo.");
            }
            if (!guestRoomDO.getRoomStatus().equals(UniversalConstant.GUEST_ROOM_TABLE_R_STATUS_NO_PERSON)) {
                log.error("delete, this room hosing.roomNo={}", roomNo);
                throw new NotChangeException("this room hosing.");
            }
            if(guestRoomMapper.updatePhyStatusByRoomNo(roomNo,
                    UniversalConstant.GUEST_ROOM_TABLE_PHY_STATUS_NOT_USING)) {
                return true;
            }
            log.error("modify, mapper insert error.");
            return false;
        } catch (NoSuchKeyException e) {
            log.error("modify, no such roomDetail.", e);
            throw new NoSuchKeyException("no such roomDetail.");
        } catch (NotChangeException e) {
            log.error("modify, this room hosing.", e);
            throw new NotChangeException("this room hosing");
        } catch (Exception e) {
            log.error("modify occur exception.", e);
            throw new InnerErrorException("add occur exception.");
        }
    }

    @Override
    public List<GuestRoomVO> queryAll(int page, int size) throws InnerErrorException {
        try {
            log.info("queryAll, page={}, size={}", page, size);
            if (page < 0 || size <= 0) {
                return null;
            }
            int count = guestRoomMapper.queryAll().size();
            PageHelper.startPage(page, size);
            List<GuestRoomDO> guestRoomDOS = guestRoomMapper.queryAll();
            log.info("queryAll, guestRoomDOS={}", guestRoomDOS.toArray());
            if (guestRoomDOS.size() == 0) {
                return null;
            }
            return guestRoomDOS.stream().map(temp ->
                    GuestRoomVO.builder()
                    .roomNo(temp.getRoomNo())
                    .roomDetail(temp.getRoomDetail())
                    .roomStatus(temp.getRoomStatus())
                    .orderNo(temp.getOrderNo())
                    .count(count)
                    .build()
            ).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("queryAll occur exception.", e);
            throw new InnerErrorException("queryAll occur exception.");
        }
    }

    @Override
    public GuestRoomVO querySome(String roomNo) throws InnerErrorException {
        try {
            log.info("queryAll, roomNo={}", roomNo);
            if (StringUtils.isBlank(roomNo)) {
                return null;
            }
            GuestRoomDO guestRoomDO = guestRoomMapper.queryByRoomNo(roomNo);
            if (guestRoomDO == null) {
                return null;
            }
            return GuestRoomVO.builder()
                    .roomNo(guestRoomDO.getRoomNo())
                    .roomStatus(guestRoomDO.getRoomStatus())
                    .roomDetail(guestRoomDO.getRoomDetail())
                    .orderNo(guestRoomDO.getOrderNo())
                    .build();
        } catch (Exception e) {
            log.error("queryAll occur exception.", e);
            throw new InnerErrorException("queryAll occur exception.");
        }
    }

    @Override
    public List<GuestRoomVO> queryByRoomKey(String roomKey, int page, int size) throws InnerErrorException {
        try {
            log.info("queryByRoomKey, roomKey={}, page={}, size={}", roomKey, page, size);
            if (StringUtils.isBlank(roomKey)) {
                log.error("queryByRoomKey, param is null");
                return null;
            }
            if (page < 0 || size <= 0) {
                log.error("queryByRoomKey, param error");
                return null;
            }
            int count = guestRoomMapper.queryByRoomDetail(roomKey).size();
            PageHelper.startPage(page, size);
            List<GuestRoomDO> guestRoomDOS = guestRoomMapper.queryByRoomDetail(roomKey);
            return guestRoomDOS.stream().map(temp -> GuestRoomVO.builder()
                    .roomNo(temp.getRoomNo())
                    .roomDetail(temp.getRoomDetail())
                    .orderNo(temp.getOrderNo())
                    .roomStatus(temp.getRoomStatus())
                    .count(count)
                    .build()
            ).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("queryByRoomKey.", e);
            throw new InnerErrorException("queryByRoomKey");
        }
    }

    @Override
    public Map<String, Integer> queryCount() throws InnerErrorException {
        try {
            Map<String, Integer> res = Maps.newHashMap();
            List<RoomInfoDO> roomInfoDOS = roomInfoMapper.queryAll();
            for (RoomInfoDO roomInfoDO : roomInfoDOS) {
                int count = guestRoomMapper.countByRoomTypeKey(roomInfoDO.getRoomTypeKey());
                res.put(roomInfoDO.getRoomTypeKey(), count);
            }
            return res;
        } catch (Exception e) {
            log.error("queryCount occur exception.", e);
            throw new InnerErrorException();
        }
    }


}
