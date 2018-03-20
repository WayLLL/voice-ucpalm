package com.voice.rest.conf.mybatis;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * 
 * @author chendi
 * mybatis 自定义配置
 */

@Configuration
@ConditionalOnClass({ SqlSessionFactory.class, SqlSessionFactoryBean.class })
@ConditionalOnBean(DataSourceConfig.class)
@EnableConfigurationProperties(MybatisProperties.class)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class MybatisConfig   {

	@Autowired
	private DruidDataSource dataSource;
	@Autowired
	private MybatisProperties mybatisProperties;	
	@Autowired(required = false)
    private Interceptor[] interceptors;
	@Autowired
	private ResourceLoader resourceLoader = new DefaultResourceLoader();
	
	
	@PostConstruct
    public void checkConfigFileExists() {
        if (this.mybatisProperties.isCheckConfigLocation()) {
            Resource resource = this.resourceLoader
                    .getResource(this.mybatisProperties.getConfig());
            Assert.state(resource.exists(),
                    "Cannot find config location: " + resource
                            + " (please add config file or check your Mybatis "
                            + "configuration)");
        }
    }
	
	/**
	 * 初始化 mybaitis sessionfactory
	 * @return
	 */
	@Bean(name = "sqlSessionFactory")
	@ConditionalOnMissingBean
    public SqlSessionFactory sqlSessionFactoryBean() {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(dataSource);
        if (StringUtils.hasText(this.mybatisProperties.getConfig())) {
            factory.setConfigLocation(
                    this.resourceLoader.getResource(this.mybatisProperties.getConfig()));
        } else {
            if (this.interceptors != null && this.interceptors.length > 0) {
                factory.setPlugins(this.interceptors);
            }
            factory.setTypeAliasesPackage(this.mybatisProperties.getTypeAliasesPackage());
            factory.setTypeHandlersPackage(this.mybatisProperties.getTypeHandlersPackage());
            factory.setMapperLocations(this.mybatisProperties.getMapperLocations());
        }
        try {
            return factory.getObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

	/**
	 * 初始化 SqlSessionTemplate mybatis操作数据库模板
	 * @param sqlSessionFactory
	 * @return
	 */
    @Bean
    @ConditionalOnMissingBean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
