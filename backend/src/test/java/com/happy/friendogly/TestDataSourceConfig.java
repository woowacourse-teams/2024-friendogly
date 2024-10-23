package com.happy.friendogly;

import com.happy.friendogly.config.datasource.DataSourceType;
import com.happy.friendogly.config.datasource.ReplicationRoutingDataSource;
import com.zaxxer.hikari.HikariDataSource;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

@Profile({"testMultiDataSource"})
@TestConfiguration
public class TestDataSourceConfig {

    private static final String WRITER_DATA_SOURCE_BEAN_NAME = "writerDataSource";
    private static final String READER_DATA_SOURCE_BEAN_NAME = "readerDataSource";
    private static final String WRITER_DATA_SOURCE_PREFIX = "spring.datasource.writer.hikari";
    private static final String READER_DATA_SOURCE_PREFIX = "spring.datasource.reader.hikari";
    private static final String ROUTING_DATA_SOURCE = "routingDataSource";

    @Primary
    @Bean(name = "dataSource")
    public DataSource dataSource(@Qualifier(ROUTING_DATA_SOURCE) DataSource routingDataSourceType) {
        return new LazyConnectionDataSourceProxy(routingDataSourceType);
    }

    @Bean(name = WRITER_DATA_SOURCE_BEAN_NAME)
    @ConfigurationProperties(prefix = WRITER_DATA_SOURCE_PREFIX)
    public DataSource writerDataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean(name = READER_DATA_SOURCE_BEAN_NAME)
    @ConfigurationProperties(prefix = READER_DATA_SOURCE_PREFIX)
    public DataSource readerDataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean(name = ROUTING_DATA_SOURCE)
    public DataSource routingDataSource(
            @Qualifier(WRITER_DATA_SOURCE_BEAN_NAME) DataSource writerDataSourceType,
            @Qualifier(READER_DATA_SOURCE_BEAN_NAME) DataSource readerDataSourceType
    ) {

        ReplicationRoutingDataSource routingDataSource = new ReplicationRoutingDataSource();

        Map<Object, Object> dataSources = new HashMap<>();

        dataSources.put(DataSourceType.WRITER, writerDataSourceType);
        dataSources.put(DataSourceType.READER, readerDataSourceType);

        routingDataSource.setTargetDataSources(dataSources);
        routingDataSource.setDefaultTargetDataSource(writerDataSourceType);

        return routingDataSource;
    }
}
