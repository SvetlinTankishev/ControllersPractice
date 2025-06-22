package org.example.config;

import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import static org.assertj.core.api.Assertions.assertThat;

public class MySQLContainerITest extends IntegrationTestBase {

    @Test
    void container_shouldBeRunning() {
        assertThat(mySQLContainer.isRunning()).isTrue();
    }

    @Test
    void dataSource_shouldBeAvailableAndConnected() throws SQLException {
        assertThat(dataSource).isNotNull();
        assertThat(dataSource.getConnection().isValid(1)).isTrue();
    }
} 