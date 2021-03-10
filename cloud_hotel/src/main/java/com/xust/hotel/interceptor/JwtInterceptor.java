package com.xust.hotel.interceptor;

import com.xust.hotel.common.dto.LoginUserPojo;
import com.xust.hotel.common.exception.AccessException;
import com.xust.hotel.common.exception.ExpiredException;
import com.xust.hotel.common.security.CryptUtil;
import com.xust.hotel.common.security.JwtConstantConfig;
import com.xust.hotel.common.security.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * @author bhj
 */
@Slf4j
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String header = request.getHeader(JwtConstantConfig.HEADER_KEY);
        if (StringUtils.isBlank(header)) {
            log.error("preHandle, header is null");
            return true;
        }
        String token = CryptUtil.decrypt(header);
        if (StringUtils.isNotBlank(token)
                && token.startsWith(JwtConstantConfig.HEADER_TOKEN_START)
                && token.endsWith(JwtConstantConfig.HEADER_TOKEN_END)) {
            try {
                token = token.substring(JwtConstantConfig.SUB_START_NUM);
                token = token.substring(0, token.length() - JwtConstantConfig.SUB_END_NUM);
                LoginUserPojo loginUserDTO = jwtUtil.getLoginUserDTO(token);
                if (loginUserDTO == null) {
                    log.error("preHandle, there are no user info, loginUserDTO is null");
                    return true;
                }
                log.info("preHandle, loginUserDTO={}", loginUserDTO.toString());
                String redisAuth = String.valueOf(redisTemplate.opsForValue().get(loginUserDTO.getUser()));
                if (StringUtils.isBlank(redisAuth)) {
                    //抛过期异常
                    throw new ExpiredException("permission expired");
                }
                if (!header.equals(redisAuth)) {
                    log.error("preHandle, header auth info is different from redis auth info" +
                            "header auth={}, redis auth={}", header, redisAuth);
                    return true;
                }
                redisTemplate.opsForValue().set(loginUserDTO.getUser(), header, 2, TimeUnit.HOURS);
                request.setAttribute(JwtConstantConfig.REQ_ATTRIBUTE_KEY, loginUserDTO);
            } catch (ExpiredException e) {
                log.error("preHandle, expired.", e);
                throw new ExpiredException("expired.");
            } catch (Exception e) {
                log.error("令牌不正确");
                throw new AccessException("令牌不正确");
            }
        }
        return true;
    }
}
