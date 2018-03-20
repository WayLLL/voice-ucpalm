package com.voice.rest.conf.mybatis;

import java.util.Properties;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 设置mybaitis扫描路径
 * @author chendi
 *
 */
@Configuration
@AutoConfigureAfter(MybatisConfig.class)
public class MyBatisMapperScannerConfig {
	@Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        //获取之前注入的beanName为sqlSessionFactory的对象
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        //指定xml配置文件的路径
        mapperScannerConfigurer.setBasePackage("com.ucpalm.voice.db.**.dao");
        Properties properties = new Properties();
        return mapperScannerConfigurer;
    }
}
