package com.xust.hotel.user.common.service;

import com.xust.hotel.user.common.vo.UserVO;
import com.xust.hotel.common.exception.InnerErrorException;
import per.bhj.xust.hotel_manager.dbo.UserDO;
import per.bhj.xust.hotel_manager.dto.UserDTO;

import java.util.List;

/**
 * @author bhj
 */
public interface UserService {


    public UserDO getUserInfoByUser(String user) throws InnerErrorException;

    /**
     * match
     * @param user
     * @param password
     * @param type
     * @return
     * @throws InnerErrorException
     */
    public boolean matchUserToPass(String user, String password, String type) throws InnerErrorException;

    /**
     * register admin user
     * @param user
     * @param password
     * @throws InnerErrorException
     * @return
     */
    public boolean registerAdmin(String user, String password) throws InnerErrorException;

    /**
     * log out
     * @param user
     * @param password
     * @param type
     * @throws Exception
     * @return
     */
    public boolean logout(String user, String password, String type) throws Exception;

    /**
     * create user
     * @param name
     * @param password
     * @param type
     * @return
     * @throws InnerErrorException
     */
    public UserDTO createUser(String name, String password, String type) throws InnerErrorException;


    /**
     * modify
     * @param user
     * @param name
     * @param password
     * @return
     * @throws InnerErrorException
     */
    public UserDTO modifyUserInfo(String user, String name, String password) throws InnerErrorException;


    /**
     * delete
     * @param user
     * @return
     * @throws InnerErrorException
     */
    public boolean deleteUser(String user) throws InnerErrorException;


    /**
     * query list
     * @param name
     * @param page
     * @param size
     * @return
     * @throws InnerErrorException
     */
    public List<UserVO> queryUser(String name, int page, int size) throws InnerErrorException;
}
