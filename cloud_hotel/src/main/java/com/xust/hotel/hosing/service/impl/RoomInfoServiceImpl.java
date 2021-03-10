package com.xust.hotel.hosing.service.impl;

import com.github.pagehelper.PageHelper;
import com.xust.hotel.common.constantAndMapper.UniversalConstant;
import com.xust.hotel.common.exception.InnerErrorException;
import com.xust.hotel.common.exception.KeyExistException;
import com.xust.hotel.common.exception.NoSuchKeyException;
import com.xust.hotel.common.exception.NotChangeException;
import com.xust.hotel.hosing.service.RoomInfoService;
import com.xust.hotel.vo.RoomDetailVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import per.bhj.xust.hotel_manager.dbo.RoomInfoDO;
import per.bhj.xust.hotel_manager.mapper.GuestRoomMapper;
import per.bhj.xust.hotel_manager.mapper.RoomInfoMapper;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 客房详情单 服务实现类
 * </p>
 *
 * @author bhj
 * @since 2021-01-03
 */
@Slf4j
@Service
public class RoomInfoServiceImpl implements RoomInfoService {

    @Resource
    private RoomInfoMapper roomInfoMapper;

    @Resource
    private GuestRoomMapper guestRoomMapper;

    @Override
    public boolean saveDynamic(RoomDetailVO data) throws InnerErrorException, KeyExistException {
        try {
            log.info("add, data={}", data.toString());
            if (!checkVO(data)) {
                log.error("add, check fail.");
                return false;
            }
            RoomInfoDO roomInfoDO = roomInfoMapper.queryByRoomTypeKey(data.getRoomKey());
            if (roomInfoDO != null) {
                log.error("saveDynamic, key exist.data={}", data.toString());
                throw new KeyExistException("key exist.");
            }
            return roomInfoMapper.saveDynamic(convertVO2DO(data));
        } catch (KeyExistException e) {
            log.error("addDynamic, key exist.", e);
            throw new KeyExistException("key exist");
        } catch (Exception e) {
            log.error("add occur exception.");
            throw new InnerErrorException("add occur exception.", e);
        }
    }

    @Override
    public RoomDetailVO modifyDynamic(RoomDetailVO data) throws InnerErrorException, NoSuchKeyException, KeyExistException, NotChangeException {
        try {
            log.info("modifyDynamic, data={}", data.toString());
            if (!checkVO(data)) {
                log.error("modifyDynamic, check fail.");
                return null;
            }
            if (roomInfoMapper.queryByRoomTypeKey(data.getRoomKey()) == null) {
                log.error("modifyDynamic, key isn't exist.");
                throw new NoSuchKeyException("this key isn't exist");
            } else if (roomInfoMapper.queryByRoomType(data.getRoomType()) != null) {
                log.error("modifyDynamic, this type exist.");
                throw new KeyExistException("this type exist");
            }
            int i = guestRoomMapper.countNotNoPersonByRoomTypeKey(data.getRoomKey());
            if (i != 0) {
                log.error("modifyDynamic, there are still people living in this type of room.");
                throw new NotChangeException("no person");
            }
            if (!roomInfoMapper.updateByRoomTypeKey(convertVO2DO(data))) {
                log.error("modifyDynamic, update error.data={}", data.toString());
                return null;
            }
            RoomInfoDO roomInfoDO = roomInfoMapper.queryByRoomTypeKey(data.getRoomKey());
            RoomDetailVO build = RoomDetailVO.builder()
                    .roomKey(roomInfoDO.getRoomTypeKey())
                    .priceUnit(roomInfoDO.getPriceUnit())
                    .roomPrice(roomInfoDO.getRoomPrice())
                    .roomType(roomInfoDO.getRoomType())
                    .build();
            log.error("build={}", build);
            return build;
        } catch (NotChangeException e) {
            log.error("modifyDynamic, there are still people living in this type of room.");
            throw new NotChangeException("no person");
        } catch (KeyExistException e) {
            log.error("modifyDynamic, ype exist.", e);
            throw new KeyExistException("type exist");
        } catch (NoSuchKeyException e) {
            log.error("modifyDynamic, no such key.", e);
            throw new NoSuchKeyException("no such key");
        } catch (Exception e) {
            log.error("modifyDynamic occur exception.", e);
            throw new InnerErrorException("modifyDynamic occur exception.", e);
        }
    }

    @Override
    public boolean delete(String roomKey) throws NotChangeException, NoSuchKeyException, InnerErrorException {
        try {
            log.info("delete, roomKey={}", roomKey);
            if (StringUtils.isBlank(roomKey)) {
                log.error("delete, roomKey is null.");
                return false;
            }
            int i = guestRoomMapper.countNotNoPersonByRoomTypeKey(roomKey);
            if (i != 0) {
                log.error("delete, there are still people living in this type of room.");
                throw new NotChangeException("no person");
            }
            if (!roomInfoMapper.deleteByRoomTypeKey(roomKey)) {
                log.error("delete, this room key isn't exist");
                throw new NoSuchKeyException("this room key isn't exist");
            }
            return i == guestRoomMapper.updatePhyStatusByRoomDetail(roomKey,
                    UniversalConstant.GUEST_ROOM_TABLE_PHY_STATUS_NOT_USING);
        } catch (NotChangeException e) {
            log.error("delete, not delete", e);
            throw new NotChangeException("no person");
        } catch (NoSuchKeyException e) {
            log.error("delete, no such key", e);
            throw new NoSuchKeyException("no such key");
        } catch (Exception e) {
            log.error("delete occur exception.", e);
            throw new InnerErrorException("delete occur exception.");
        }
    }

    @Override
    public List<RoomDetailVO> query(int page, int size) throws InnerErrorException {
        try {
            log.info("query, page={}, size={}", page, size);
            if (page < 0 || size <= 0) {
                log.error("query, param error.page={}, size={}", page, size);
                return null;
            }
            int count = roomInfoMapper.queryAll().size();
            PageHelper.startPage(page, size);
            List<RoomInfoDO> roomInfoDOS = roomInfoMapper.queryAll();
            if (roomInfoDOS.size() == 0) {
                return null;
            }
            return roomInfoDOS.stream().map(temp -> RoomDetailVO.builder()
                    .roomType(temp.getRoomType())
                    .roomPrice(temp.getRoomPrice())
                    .roomKey(temp.getRoomTypeKey())
                    .priceUnit(temp.getPriceUnit())
                    .count(count)
                    .build()
            ).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("query occur exception");
            throw new InnerErrorException("query occur exception.", e);
        }
    }

    @Override
    public RoomDetailVO queryByRoomKeyOrRoomType(String roomKey, String roomType) throws InnerErrorException {
        try {
            log.info("queryByRoomKeyOrRoomType, roomKey={}, roomType={}", roomKey, roomType);
            if (StringUtils.isBlank(roomKey) && StringUtils.isBlank(roomType)) {
                log.error("queryByRoomKeyOrRoomType, param error.");
                return null;
            }
            RoomInfoDO roomInfoDO;
            if (StringUtils.isBlank(roomKey)) {
                roomInfoDO = roomInfoMapper.queryByRoomType(roomType);
            } else {
                roomInfoDO = roomInfoMapper.queryByRoomTypeKey(roomKey);
            }
            if (roomInfoDO == null) {
                return null;
            }
            return RoomDetailVO.builder()
                    .roomKey(roomInfoDO.getRoomTypeKey())
                    .roomType(roomInfoDO.getRoomType())
                    .roomPrice(roomInfoDO.getRoomPrice())
                    .priceUnit(roomInfoDO.getPriceUnit())
                    .build();
        } catch (Exception e) {
            log.error("queryByRoomKeyOrRoomType occur exception", e);
            throw new InnerErrorException("queryByRoomKeyOrRoomType occur exception");
        }
    }


    /**
     * convert
     */
    private RoomInfoDO convertVO2DO(RoomDetailVO roomDetailVO) {
        return RoomInfoDO.builder()
                .roomTypeKey(roomDetailVO.getRoomKey())
                .roomType(roomDetailVO.getRoomType())
                .roomPrice(roomDetailVO.getRoomPrice())
                .priceUnit(UniversalConstant.DEFAULT_ROOM_PRICE_UNIT)
                .build();
    }

    /**
     * check vo
     */
    private boolean checkVO(RoomDetailVO data) {
        if (StringUtils.isBlank(data.getRoomKey())) {
            log.error("add, roomKey is null. data={}", data.toString());
            return false;
        }
        if (StringUtils.isBlank(data.getRoomType())) {
            log.error("add, roomType is null. data={}", data.toString());
            return false;
        }
        if (data.getRoomPrice() == null) {
            log.error("add, roomPrice is null, data={}", data.toString());
            return false;
        }
        return true;
    }
}
