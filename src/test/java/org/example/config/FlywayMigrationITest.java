package org.example.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FlywayMigrationITest extends IntegrationTestBase {

    @Autowired
    private DataSource dataSource;

    @Test
    void tables_shouldBeCreatedByFlyway() throws SQLException {
        List<String> tableNames = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            ResultSet tables = connection.getMetaData().getTables(null, null, "%", new String[]{"TABLE"});
            while (tables.next()) {
                tableNames.add(tables.getString("TABLE_NAME").toLowerCase());
            }
        }
        assertThat(tableNames).contains("animal", "car", "gov_employee");
    }
} 