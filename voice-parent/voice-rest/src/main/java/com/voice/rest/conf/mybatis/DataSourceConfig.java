package com.voice.rest.conf.mybatis;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * 数据源配置中心
 * 此处使用DruidDataSource 作为数据源 以及 连接池
 * @author chendi
 *
 */
@Configuration
public class DataSourceConfig {

	 	@Value("${spring.datasource.url}")    
	    private String dbUrl;    
	    @Value("${spring.datasource.username}")    
	    private String username;    
	    @Value("${spring.datasource.password}")    
	    private String password;    
	    @Value("${spring.datasource.driverClassName}")    
	    private String driverClassName;    
	    @Value("${spring.datasource.initialSize}")    
	    private int initialSize;    
	    @Value("${spring.datasource.minIdle}")    
	    private int minIdle;    
	    @Value("${spring.datasource.maxActive}")    
	    private int maxActive;    
	    @Value("${spring.datasource.maxWait}")    
	    private int maxWait;    
	    @Value("${spring.datasource.timeBetweenEvictionRunsMillis}")    
	    private int timeBetweenEvictionRunsMillis;    
	    @Value("${spring.datasource.minEvictableIdleTimeMillis}")    
	    private int minEvictableIdleTimeMillis;    
	    @Value("${spring.datasource.validationQuery}")    
	    private String validationQuery;    
	    @Value("${spring.datasource.testWhileIdle}")    
	    private boolean testWhileIdle;    
	    @Value("${spring.datasource.testOnBorrow}")    
	    private boolean testOnBorrow;    
	    @Value("${spring.datasource.testOnReturn}")    
	    private boolean testOnReturn;    
	    @Value("${spring.datasource.poolPreparedStatements}")    
	    private boolean poolPreparedStatements;    
	    @Value("${spring.datasource.maxPoolPreparedStatementPerConnectionSize}")    
	    private int maxPoolPreparedStatementPerConnectionSize;    
	    @Value("${spring.datasource.filters}")    
	    private String filters;    
	    @Value("${spring.datasource.connectionProperties}")    
	    private String connectionProperties;    
	    @Value("${spring.datasource.useGlobalDataSourceStat}")    
	    private boolean useGlobalDataSourceStat;  
    
    @Bean
    public DruidDataSource dataSource(){
    	DruidDataSource dataSource = new DruidDataSource();
    	 dataSource.setUrl(this.dbUrl);    
         dataSource.setUsername(username);    
         dataSource.setPassword(password);    
         dataSource.setDriverClassName(driverClassName);    
     
         //configuration    
         dataSource.setInitialSize(initialSize);    
         dataSource.setMinIdle(minIdle);    
         dataSource.setMaxActive(maxActive);    
         dataSource.setMaxWait(maxWait);    
         dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);    
         dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);    
         dataSource.setValidationQuery(validationQuery);    
         dataSource.setTestWhileIdle(testWhileIdle);    
         dataSource.setTestOnBorrow(testOnBorrow);    
         dataSource.setTestOnReturn(testOnReturn);    
         dataSource.setPoolPreparedStatements(poolPreparedStatements);    
         dataSource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);    
         dataSource.setUseGlobalDataSourceStat(useGlobalDataSourceStat);    
         try {    
        	 dataSource.setFilters(filters);    
         } catch (SQLException e) {    
             System.err.println("druid configuration initialization filter: "+ e);    
         }    
         dataSource.setConnectionProperties(connectionProperties);    
         return dataSource;    
    }
	
}
