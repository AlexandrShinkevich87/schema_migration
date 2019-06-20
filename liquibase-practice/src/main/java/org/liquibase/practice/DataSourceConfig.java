package org.liquibase.practice;

import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataSourceConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.primary")
    public DataSourceProperties primaryDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    public DataSource primaryDataSource(DataSourceProperties primaryDataSourceProperties) {
        return createDataSource(primaryDataSourceProperties);
    }

    private static DataSource createDataSource(DataSourceProperties properties) {
        log.info("Initializing connection pool: {}. Min idle connections: {}. Max connections: {}",
                properties.poolName, properties.minIdle, properties.maxPoolSize);

        val hds = DataSourceBuilder.create()
                .url(properties.url)
                .username(properties.username)
                .password(properties.password)
                .type(HikariDataSource.class)
                .build();
        hds.setPoolName(properties.poolName);
        hds.setSchema(properties.schema);
        if (properties.minIdle != null) {
            hds.setMinimumIdle(properties.minIdle);
        }
        hds.setMaximumPoolSize(properties.maxPoolSize);
        hds.setLeakDetectionThreshold(properties.leakDetectionThreshold);
        return hds;
    }

    @Bean
    @ConfigurationProperties("spring.datasource.liquibase")
    public DataSourceProperties liquibaseDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @LiquibaseDataSource
    public DataSource liquibaseDataSource(DataSourceProperties liquibaseDataSourceProperties) {
        return createDataSource(liquibaseDataSourceProperties);
    }

    @Setter
    private class DataSourceProperties {
        private String url;
        private String schema;
        private String username;
        private String password;
        private String poolName;
        private Integer minIdle;
        private Integer maxPoolSize;
        private Integer leakDetectionThreshold;
    }
}
