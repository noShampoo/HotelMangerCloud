package com.xust.hotel.user.controller;

import com.google.common.collect.Maps;
import com.xust.hotel.user.common.vo.UserVO;
import com.xust.hotel.common.dto.LoginUserPojo;
import com.xust.hotel.common.exception.InnerErrorException;
import com.xust.hotel.common.restful.RequestParam;
import com.xust.hotel.common.restful.Result;
import com.xust.hotel.common.restful.StatusEnum;
import com.xust.hotel.common.security.AccessUtil;
import com.xust.hotel.common.security.CryptUtil;
import com.xust.hotel.common.security.JwtConstantConfig;
import com.xust.hotel.common.security.JwtUtil;
import com.xust.hotel.user.common.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import per.bhj.xust.hotel_manager.dbo.UserDO;
import per.bhj.xust.hotel_manager.dto.UserDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author bhj
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    /**
     * get login info by token
     */
    @PostMapping("/login/getInfo")
    public Result getLoginInfo(@RequestBody RequestParam<Map<String, String>> requestParam) throws InnerErrorException {
        if (requestParam == null || MapUtils.isEmpty(requestParam.getData())) {
            log.error("getLoginInfo, param is null.");
            return new Result(true, StatusEnum.PARAM_ERROR, "param is null", null);
        }
        String token = requestParam.getData().get("token");
        Set<String> users = redisTemplate.keys("*");
        if (CollectionUtils.isEmpty(users)) {
            log.error("getLoginInfo, redis has no user");
            return new Result(true, StatusEnum.ERROR, null, null);
        }
        token = CryptUtil.decrypt(token);
        if (token == null) {
            log.error("getLoginInfo, decrypt error.token={}", token);
            return new Result(true, StatusEnum.ERROR, null, null);
        }
        token = token.substring(JwtConstantConfig.SUB_START_NUM);
        token = token.substring(0, token.length() - JwtConstantConfig.SUB_END_NUM);
        LoginUserPojo loginUserDTO = jwtUtil.getLoginUserDTO(token);
        if (loginUserDTO != null && users.contains(loginUserDTO.getUser())) {
            UserDO userInfoByUser = userService.getUserInfoByUser(loginUserDTO.getUser());
            if (userInfoByUser == null) {
                return new Result(true, StatusEnum.ERROR, null, null);
            }
            userInfoByUser.setPassword(loginUserDTO.getPassword());
            return new Result(true, StatusEnum.OK, null, userInfoByUser);
        }
        log.error("getLoginInfo, no login info");
        return new Result(true, StatusEnum.ERROR, null, null);
    }


    /**
     * register admin
     */
    @PostMapping("/login/register/admin")
    public Result register(@RequestBody RequestParam<Map<String, String>> requestParam,
                           HttpServletRequest request) throws InnerErrorException {
        String header = request.getHeader(JwtConstantConfig.REGISTER_ADMIN_HEADER_KEY);
        if (StringUtils.isBlank(header)) {
            log.error("register, header is null.");
            return new Result(true, StatusEnum.PARAM_ERROR, null, null);
        }
        if (!header.equals(JwtConstantConfig.REGISTER_ADMIN_HEADER_VALUE)) {
            log.error("register, header error");
            return new Result(true, StatusEnum.PARAM_ERROR, null, null);
        }
        if (requestParam == null || MapUtils.isEmpty(requestParam.getData())) {
            log.error("register, param is null.");
            return new Result(true, StatusEnum.PARAM_ERROR, "param is null", null);
        }
        Map<String, String> data = requestParam.getData();
        String user = data.get("user");
        String password = data.get("password");
        if (StringUtils.isBlank(user) || StringUtils.isBlank(password)) {
            log.error("register, param is null.user={}, password={}", user, password);
            return new Result(true, StatusEnum.PARAM_ERROR, "data error", null);
        }
        if (userService.registerAdmin(user, password)) {
            return new Result(true, StatusEnum.OK, null, null);
        }
        return new Result(true, StatusEnum.ERROR, null, null);
    }

    /**
     * login
     */
    @PostMapping("/login/in")
    public Result login(@RequestBody RequestParam<Map<String, String>> requestParam) throws InnerErrorException {
        if (requestParam == null || MapUtils.isEmpty(requestParam.getData())) {
            log.error("login, param is null.");
            return new Result(true, StatusEnum.PARAM_ERROR, "param is null", null);
        }
        Map<String, String> data = requestParam.getData();
        String user = data.get("user");
        String password = data.get("password");
        String type = data.get("type");
        if (StringUtils.isBlank(user) || StringUtils.isBlank(password) || StringUtils.isBlank(type)) {
            log.error("login, param is null.user={}, password={}, type={}", user, password, type);
            return new Result(true, StatusEnum.PARAM_ERROR, "data error", null);
        }
        if (!type.equals(JwtConstantConfig.USER_ROLE_NORMAL) && !type.equals(JwtConstantConfig.USER_ROLE_ADMIN)) {
            log.error("login, type error.type={}", type);
            return new Result(true, StatusEnum.PARAM_ERROR, "type error", null);
        }
        if (!userService.matchUserToPass(user, password, type)) {
            log.error("login, match fail.user={}, password={}", user, password);
            return new Result(true, StatusEnum.LOGIN_ERROR, null, null);
        }
        String jwt =
                JwtConstantConfig.HEADER_TOKEN_START + jwtUtil.createJwt(user, password, type) + JwtConstantConfig.HEADER_TOKEN_END;
        jwt = CryptUtil.encrypt(jwt);
        redisTemplate.opsForValue().set(user, jwt, 2, TimeUnit.HOURS);
        Map<String, String> map = Maps.newHashMap();
        map.put("authHeaderKey", JwtConstantConfig.HEADER_KEY);
        map.put("authHeaderValue", jwt);
        map.put("user", user);
        return new Result(true, StatusEnum.OK, null, map);
    }

    /**
     * log out
     */
    @PostMapping("/login/out")
    public Result logout(@RequestBody RequestParam<Map<String, String>> requestParam) throws Exception {
        if (requestParam == null || MapUtils.isEmpty(requestParam.getData())) {
            log.error("logout, param is null.");
            return new Result(true, StatusEnum.PARAM_ERROR, "param is null", null);
        }
        Map<String, String> data = requestParam.getData();
        String user = data.get("user");
        String password = data.get("password");
        String type = data.get("type");
        if (StringUtils.isBlank(user) || StringUtils.isBlank(password) || StringUtils.isBlank(type)) {
            log.error("logout, param is null.user={}, password={}, type={}", user, password, type);
            return new Result(true, StatusEnum.PARAM_ERROR, "data error", null);
        }
        if (!type.equals(JwtConstantConfig.USER_ROLE_NORMAL) && !type.equals(JwtConstantConfig.USER_ROLE_ADMIN)) {
            log.error("logout, type error.type={}", type);
            return new Result(true, StatusEnum.PARAM_ERROR, "type error", null);
        }
        if (!userService.logout(user, password, type)) {
            log.error("logout, match fail.user={}, password={}", user, password);
            return new Result(true, StatusEnum.LOGIN_ERROR, null, null);
        }
        return new Result(true, StatusEnum.OK, null, null);
    }

    /**
     * admin user add normal user
     */
    @PostMapping("admin/add")
    public Result add(@RequestBody RequestParam<Map<String, String>> requestParam,
                      HttpServletRequest request) throws InnerErrorException {
        if (requestParam == null || MapUtils.isEmpty(requestParam.getData())) {
            log.error("add, param is null.");
            return new Result(true, StatusEnum.PARAM_ERROR, "param is null", null);
        }
        Map<String, String> data = requestParam.getData();
        String name = data.get("name");
        String password = data.get("password");
        String type = data.get("type");
        if (StringUtils.isBlank(name) || StringUtils.isBlank(password) || StringUtils.isBlank(type)) {
            log.error("add, param is null.name={}, password={}, type={}", name, password, type);
            return new Result(true, StatusEnum.PARAM_ERROR, "data error", null);
        }
        if (!type.equals(JwtConstantConfig.USER_ROLE_NORMAL)) {
            log.error("add, type error.type={}", type);
            return new Result(true, StatusEnum.PARAM_ERROR, "type error.type:" + type, null);
        }
        if (!AccessUtil.checkAccess(request, JwtConstantConfig.USER_ROLE_ADMIN)) {
            log.error("add, access error");
            return new Result(true, StatusEnum.ACCESS_ERROR, null, null);
        }
        UserDTO userDTO = userService.createUser(name, password, type);
        if (userDTO == null) {
            log.error("add, create user error.");
            return new Result(true, StatusEnum.ERROR, null, null);
        }
        if (StringUtils.isBlank(userDTO.getUser()) || StringUtils.isBlank(userDTO.getName())
                || StringUtils.isBlank(userDTO.getType())) {
            log.error("add, result error.userDTO={}", userDTO.toString());
            return new Result(true, StatusEnum.ERROR, null, null);
        }
        return new Result(true, StatusEnum.OK, null, userDTO);
    }

    /**
     * modify user
     */
    @PostMapping("/admin/modify")
    public Result modify(@RequestBody RequestParam<Map<String, String>> requestParam,
                         HttpServletRequest request) throws InnerErrorException {
        if (requestParam == null || MapUtils.isEmpty(requestParam.getData())) {
            log.error("modify, param is null.");
            return new Result(true, StatusEnum.PARAM_ERROR, "param is null", null);
        }
        Map<String, String> data = requestParam.getData();
        String name = data.get("name");
        String password = data.get("password");
        String user = data.get("user");
        if (StringUtils.isBlank(name) || StringUtils.isBlank(password) || StringUtils.isBlank(user)) {
            log.error("modify, param is null.name={}, password={}, user={}", name, password, user);
            return new Result(true, StatusEnum.PARAM_ERROR, "data error", null);
        }
        if (!AccessUtil.checkAccess(request, JwtConstantConfig.USER_ROLE_ADMIN)) {
            log.error("modify, access error");
            return new Result(true, StatusEnum.ACCESS_ERROR, null, null);
        }
        UserDTO userDTO = userService.modifyUserInfo(user, name, password);
        if (userDTO == null) {
            log.error("modify, modify user error.");
            return new Result(true, StatusEnum.ERROR, null, null);
        }
        if (StringUtils.isBlank(userDTO.getUser()) || StringUtils.isBlank(userDTO.getName())
                || StringUtils.isBlank(userDTO.getPassword())) {
            log.error("modify, result error.userDTO={}", userDTO.toString());
            return new Result(true, StatusEnum.ERROR, null, null);
        }
        return new Result(true, StatusEnum.OK, null, userDTO);
    }

    /**
     * delete
     */
    @PostMapping("/admin/delete")
    public Result delete(@RequestBody RequestParam<Map<String, String>> requestParam,
                         HttpServletRequest request) throws InnerErrorException {
        if (requestParam == null || MapUtils.isEmpty(requestParam.getData())) {
            log.error("delete, param is null.");
            return new Result(true, StatusEnum.PARAM_ERROR, "param is null", null);
        }
        Map<String, String> data = requestParam.getData();
        String user = data.get("user");
        if (StringUtils.isBlank(user)) {
            log.error("delete, param is null.user={}", user);
            return new Result(true, StatusEnum.PARAM_ERROR, "data error", null);
        }
        if (!AccessUtil.checkAccess(request, JwtConstantConfig.USER_ROLE_ADMIN)) {
            log.error("delete, access error");
            return new Result(true, StatusEnum.ACCESS_ERROR, null, null);
        }
        if (!userService.deleteUser(user)) {
            log.error("delete, delete fail.");
            return new Result(true, StatusEnum.ERROR, null, null);
        }
        return new Result(true, StatusEnum.OK, null, null);
    }

    /**
     * query all
     */
    @GetMapping("/get/all/{page}/{size}")
    public Result getAll(@PathVariable("page")int page,
                         @PathVariable("size")int size,
                         HttpServletRequest request) throws InnerErrorException {
        if (page < 0 || size <= 0) {
            log.error("getAll, param error.page={}, size={}", page, size);
            return new Result(true, StatusEnum.PARAM_ERROR, "page:" + page + ",size:" + size, null);
        }
        if (!AccessUtil.checkAccess(request, JwtConstantConfig.USER_ROLE_ADMIN)) {
            log.error("delete, access error");
            return new Result(true, StatusEnum.ACCESS_ERROR, null, null);
        }
        List<UserVO> userVOList = userService.queryUser(null, page, size);
        return new Result(true, StatusEnum.OK, null, userVOList);
    }

    /**
     * query some user
     */
    @GetMapping("get/some/{name}/{page}/{size}")
    public Result getSomeUser(@PathVariable("name")String name,
                              @PathVariable("page")int page,
                              @PathVariable("size")int size,
                              HttpServletRequest request) throws InnerErrorException {
        if (StringUtils.isBlank(name)) {
            log.error("getSomeUser, name is null.");
            return new Result(true, StatusEnum.PARAM_ERROR, "name is null", null);
        }
        if (page < 0 || size <= 0) {
            log.error("getAll, param error.page={}, size={}", page, size);
            return new Result(true, StatusEnum.PARAM_ERROR, "page:" + page + ",size:" + size, null);
        }
        if (!AccessUtil.checkAccess(request, JwtConstantConfig.USER_ROLE_ADMIN)) {
            log.error("delete, access error");
            return new Result(true, StatusEnum.ACCESS_ERROR, null, null);
        }
        List<UserVO> userVOList = userService.queryUser(name, page, size);
        return new Result(true, StatusEnum.OK, null, userVOList);
    }

    /**
     * normal user modify himself
     */
    @PostMapping("/normal/modify")
    public Result normalModify(@RequestBody RequestParam<Map<String, String>> requestParam,
                               HttpServletRequest request) throws InnerErrorException {
        if (requestParam == null || MapUtils.isEmpty(requestParam.getData())) {
            log.error("normalModify, param is null.");
            return new Result(true, StatusEnum.PARAM_ERROR, "param is null", null);
        }
        Map<String, String> data = requestParam.getData();
        String name = data.get("name");
        String password = data.get("password");
        String user = data.get("user");
        if (StringUtils.isBlank(name) || StringUtils.isBlank(password) || StringUtils.isBlank(user)) {
            log.error("normalModify, param is null.name={}, password={}, user={}", name, password, user);
            return new Result(true, StatusEnum.PARAM_ERROR, "data error", null);
        }
        if (!AccessUtil.checkAccess(request, JwtConstantConfig.USER_ROLE_NORMAL)) {
            log.error("normalModify, access error");
            return new Result(true, StatusEnum.ACCESS_ERROR, null, null);
        }
        UserDTO userDTO = userService.modifyUserInfo(user, name, password);
        if (userDTO == null) {
            log.error("modify, modify user error.");
            return new Result(true, StatusEnum.ERROR, null, null);
        }
        if (StringUtils.isBlank(userDTO.getUser()) || StringUtils.isBlank(userDTO.getName())
                || StringUtils.isBlank(userDTO.getPassword())) {
            log.error("modify, result error.userDTO={}", userDTO.toString());
            return new Result(true, StatusEnum.ERROR, null, null);
        }
        return new Result(true, StatusEnum.OK, null, userDTO);
    }
}
