package com.pgs.spark.bigdata.config;

import com.google.common.collect.ImmutableMap;
import com.pgs.spark.bigdata.entities.AbstractEntity;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:database.properties")
@EnableJpaRepositories("com.pgs.spark.bigdata.repository")
@EntityScan(basePackageClasses = AbstractEntity.class)
public class DatabaseConfiguration {

    private static final String PROPERTY_NAME_HIBERNATE_DIALECT = "hibernate.dialect";

    private static final String PROPERTY_NAME_HIBERNATE_FORMAT_SQL = "hibernate.format_sql";

    private static final String PROPERTY_NAME_HIBERNATE_HBM2DDL_AUTO = "hibernate.hbm2ddl.auto";

    private static final String PROPERTY_NAME_HIBERNATE_EJB_NAMING_STRATEGY = "hibernate.ejb.naming_strategy";

    private static final String PROPERTY_NAME_HIBERNATE_SHOW_SQL = "hibernate.show_sql";

    private static final String PROPERTY_NAME_HIBERNATE_C3P0_MIN_SIZE = "hibernate.c3p0.min_size";

    private static final String PROPERTY_NAME_HIBERNATE_C3P0_MAX_SIZE = "hibernate.c3p0.max_size";

    private static final String PROPERTY_NAME_HIBERNATE_C3P0_TIMEOUT = "hibernate.c3p0.timeout";

    private static final String PROPERTY_NAME_HIBERNATE_C3P0_MAX_STATEMENTS = "hibernate.c3p0.max_statements";

    private static final String PROPERTY_NAME_HIBERNATE_C3P0_IDLE_TEST_PERIOD = "hibernate.c3p0.idle_test_period";

    @Value("${db.driver}")
    private String dbDriver;

    @Value("${db.url}")
    private String dbUrl;

    @Value("${db.username}")
    private String dbUsername;

    @Value("${db.password}")
    private String dbPassword;

    @Value("${" + PROPERTY_NAME_HIBERNATE_DIALECT + "}")
    private String dialect;

    @Value("${" + PROPERTY_NAME_HIBERNATE_FORMAT_SQL + "}")
    private String formatSql;

    @Value("${" + PROPERTY_NAME_HIBERNATE_HBM2DDL_AUTO + "}")
    private String hbm2ddl;

    @Value("${" + PROPERTY_NAME_HIBERNATE_EJB_NAMING_STRATEGY + "}")
    private String namingStrategy;

    @Value("${" + PROPERTY_NAME_HIBERNATE_SHOW_SQL + "}")
    private String showSql;

    @Value("${" + PROPERTY_NAME_HIBERNATE_C3P0_MIN_SIZE + "}")
    private String minSize;

    @Value("${" + PROPERTY_NAME_HIBERNATE_C3P0_MAX_SIZE + "}")
    private String maxSize;

    @Value("${" + PROPERTY_NAME_HIBERNATE_C3P0_TIMEOUT + "}")
    private String timeout;

    @Value("${" + PROPERTY_NAME_HIBERNATE_C3P0_MAX_STATEMENTS + "}")
    private String maxStatements;

    @Value("${" + PROPERTY_NAME_HIBERNATE_C3P0_IDLE_TEST_PERIOD + "}")
    private String idleTestPeriod;

    @Bean(destroyMethod = "close")
    public HikariDataSource dataSource() {
        HikariConfig dataSourceConfig = new HikariConfig();
        dataSourceConfig.setDriverClassName(dbDriver);
        dataSourceConfig.setJdbcUrl(dbUrl);
        dataSourceConfig.setUsername(dbUsername);
        dataSourceConfig.setPassword(dbPassword);

        return new HikariDataSource(dataSourceConfig);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactoryBean.setPackagesToScan(AbstractEntity.class.getPackage().getName());

        entityManagerFactoryBean.setJpaProperties(jpaProperties());

        return entityManagerFactoryBean;
    }

    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

    private Properties jpaProperties() {
        Properties jpaProperties = new Properties();
        jpaProperties.putAll(new ImmutableMap.Builder<>()
                .put(PROPERTY_NAME_HIBERNATE_DIALECT, dialect)
                .put(PROPERTY_NAME_HIBERNATE_HBM2DDL_AUTO, hbm2ddl)
                .put(PROPERTY_NAME_HIBERNATE_EJB_NAMING_STRATEGY, namingStrategy)

                .put(PROPERTY_NAME_HIBERNATE_SHOW_SQL, showSql)
                .put(PROPERTY_NAME_HIBERNATE_FORMAT_SQL, formatSql)

                //C3P0 configuration
                .put(PROPERTY_NAME_HIBERNATE_C3P0_MIN_SIZE, minSize)
                .put(PROPERTY_NAME_HIBERNATE_C3P0_MAX_SIZE, maxSize)
                .put(PROPERTY_NAME_HIBERNATE_C3P0_TIMEOUT, timeout)
                .put(PROPERTY_NAME_HIBERNATE_C3P0_MAX_STATEMENTS, maxStatements)
                .put(PROPERTY_NAME_HIBERNATE_C3P0_IDLE_TEST_PERIOD, idleTestPeriod)
                .build());

        return jpaProperties;
    }
}
