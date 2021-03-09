package com.xust.hotel;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import com.alibaba.nacos.spring.context.annotation.config.EnableNacosConfig;
import com.xust.hotel.common.security.JwtUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author bhj
 */
@EnableDubbo
@EnableNacosConfig
@SpringBootApplication
@EnableTransactionManagement
@MapperScan("per.bhj.xust.hotel_manager.mapper")
@NacosConfigurationProperties(dataId = "per.bhj.xust.hotel.manager.application", groupId = "XUST_HOTEL_MANAGER")
public class UserCloudApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserCloudApplication.class);
    }

    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
