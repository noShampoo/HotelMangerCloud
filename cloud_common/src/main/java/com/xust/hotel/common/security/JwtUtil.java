package com.xust.hotel.common.security;

import com.xust.hotel.common.dto.LoginUserPojo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

/**
 * @author bhj
 */
public class JwtUtil {

    @Value("${jwt.config.key}")
    private String key;

    @Value("${jwt.config.ttl}")
    private long ttl;

    /**
     * 生成jwt
     * @param id id
     * @param subject sub
     * @param roles role
     * @return jwt token
     */
    public String createJwt(String id, String subject, String roles) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        JwtBuilder builder = Jwts.builder().setId(id)
                .setSubject(subject)
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS256, key).claim("roles", roles);
        if (ttl > 0) {
            builder.setExpiration(new Date(nowMillis + ttl));
        }
        return builder.compact();
    }

    /**
     * 解析jwt
     * @param token token
     * @return claim
     */
    public Claims parseJwt(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 获取登录信息
     * @param token token
     * @return user info
     */
    public LoginUserPojo getLoginUserDTO(String token) {
        Claims claims = parseJwt(token);
        return LoginUserPojo.builder()
                .user(String.valueOf(claims.get("jti")))
                .password(String.valueOf(claims.get("sub")))
                .roles(String.valueOf(claims.get("roles")))
                .build();
    }
}
