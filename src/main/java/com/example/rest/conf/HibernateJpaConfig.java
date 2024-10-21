package com.example.rest.conf;

import org.hibernate.cfg.JdbcSettings;
import org.hibernate.cfg.SchemaToolingSettings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@PropertySource("classpath:/db.properties")
public class HibernateJpaConfig {
    @Value("${databasePlatform}")
    private String databasePlatform;

    @Value("${show-sql}")
    private String showSql;

    @Value("${ddl-auto}")
    private String ddlAuto;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        emf.setJpaVendorAdapter(vendorAdapter);

        emf.setPackagesToScan("com.example.rest.entity");

        Properties properties = new Properties();
        properties.put(JdbcSettings.DIALECT, databasePlatform);
        properties.put(JdbcSettings.SHOW_SQL, showSql);
        properties.put(SchemaToolingSettings.HBM2DDL_AUTO, ddlAuto);

        emf.setJpaProperties(properties);

        return emf;
    }
}
