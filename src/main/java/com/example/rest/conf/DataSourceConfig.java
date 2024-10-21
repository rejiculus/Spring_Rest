package com.example.rest.conf;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

@PropertySource("classpath:/db.properties")
@Configuration
public class DataSourceConfig {

    @Value("${url}")
    private String jdbcUrl;
    @Value("${user}")
    private String username;
    @Value("${password}")
    private String password;
    @Value("${maximumPoolSize}")
    private int maximumPoolSize;
    @Value("${minimumIdle}")
    private int minimumIdle;
    @Value("${maxLifetime}")
    private int maxLifetime;
    @Value("${connectionTimeout}")
    private int connectionTimeout;
    @Value("${idleTimeout}")
    private int idleTimeout;
    @Value("${poolName}")
    private String poolName;
    @Value("${dataSourceClassName}")
    private String dataSourceClassName;

    @Bean
    public DataSource dataSource() {
        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setJdbcUrl(jdbcUrl);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.setMaximumPoolSize(maximumPoolSize);
        hikariConfig.setMinimumIdle(minimumIdle);
        hikariConfig.setIdleTimeout(idleTimeout);
        hikariConfig.setMaxLifetime(maxLifetime);
        hikariConfig.setConnectionTimeout(connectionTimeout);
        hikariConfig.setPoolName(poolName);
        hikariConfig.setDriverClassName(dataSourceClassName);

        return new HikariDataSource(hikariConfig);
    }
}
