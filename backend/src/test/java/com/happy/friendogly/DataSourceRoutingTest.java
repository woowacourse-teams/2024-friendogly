package com.happy.friendogly;

import static org.assertj.core.api.Assertions.assertThat;

import com.happy.friendogly.config.datasource.DataSourceType;
import com.happy.friendogly.config.datasource.ReplicationRoutingDataSource;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class DataSourceRoutingTest {

    private static final String TEST_METHOD_NAME = "determineCurrentLookupKey";

    @Test
    @DisplayName("쓰기전용 트랜잭션이면 Writer 데이터소스가 바운딩된다.")
    @Transactional
    void writeOnlyTransactionTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ReplicationRoutingDataSource replicationRoutingDataSource = new ReplicationRoutingDataSource();

        Method determineCurrentLookupKey = ReplicationRoutingDataSource.class.getDeclaredMethod(TEST_METHOD_NAME);
        determineCurrentLookupKey.setAccessible(true);

        DataSourceType dataSourceType = (DataSourceType) determineCurrentLookupKey
                .invoke(replicationRoutingDataSource);

        assertThat(dataSourceType).isEqualTo(DataSourceType.WRITER);
    }

    @Test
    @DisplayName("readOnly 트랜잭션이면 redaer 데이터소스가 바운딩된다.")
    @Transactional(readOnly = true)
    void readOnlyTransactionTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ReplicationRoutingDataSource replicationRoutingDataSource = new ReplicationRoutingDataSource();

        Method determineCurrentLookupKey = ReplicationRoutingDataSource.class.getDeclaredMethod(TEST_METHOD_NAME);
        determineCurrentLookupKey.setAccessible(true);

        DataSourceType dataSourceType = (DataSourceType) determineCurrentLookupKey
                .invoke(replicationRoutingDataSource);

        assertThat(dataSourceType).isEqualTo(DataSourceType.READER);
    }
}
