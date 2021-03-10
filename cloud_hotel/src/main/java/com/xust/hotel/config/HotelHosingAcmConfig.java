package com.xust.hotel.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @author bhj
 */
@Configuration
public class HotelHosingAcmConfig {

    @NacosValue(value = "${spring.datasource.username}")
    private String userName;

    @NacosValue(value = "${spring.datasource.password}")
    private String password;

    @NacosValue(value = "${spring.datasource.url}")
    private String url;

    @NacosValue(value = "${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Bean("customerDataSource")
    public DataSource customerDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(url);
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUsername(userName);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean("customerSqlSessionFactory")
    public SqlSessionFactory customerSqlSessionFactory(@Qualifier("customerDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(
                "classpath*:mapper/*.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean("customerDataSourceTransactionManager")
    public DataSourceTransactionManager customerDataSourceTransactionManager(@Qualifier("customerDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean("customerSqlSessionTemplate")
    public SqlSessionTemplate customerSqlSessionTemplate(@Qualifier("customerSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
