package com.happy.friendogly;

import static org.assertj.core.api.Assertions.assertThat;

import com.happy.friendogly.config.datasource.DataSourceConfig;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Import(DataSourceConfig.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("prod")
public class DataSourceRoutingTest {


    @Autowired
    private ApplicationContext applicationContext;

    @DisplayName("읽기 전용 트랜잭션이 아니면, Writer DB 데이터소스가 바운딩된다.")
    @Test
    @Transactional(readOnly = false)
    void writeOnlyTransactionTest() throws SQLException {
        DataSource dataSource = applicationContext.getBean(DataSource.class);
        try (Connection connection = dataSource.getConnection()) {
            String actual = connection.getMetaData().getURL();
            assertThat(actual).contains("writer");
        }
    }

    @Test
    @DisplayName("읽기전용 트랜잭션이면 reader DB 데이터소스가 바운딩된다.")
    @Transactional(readOnly = true)
    void readOnlyTransactionTest() throws SQLException {
        DataSource dataSource = applicationContext.getBean(DataSource.class);
        try (Connection connection = dataSource.getConnection()) {
            String actual = connection.getMetaData().getURL();
            assertThat(actual).contains("reader");
        }
    }
}
