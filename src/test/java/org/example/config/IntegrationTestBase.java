package org.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;

import javax.sql.DataSource;

@ActiveProfiles("test")
@SpringBootTest
public abstract class IntegrationTestBase {

    @Autowired
    protected DataSource dataSource;

    @ServiceConnection
    protected static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("controllers_practice")
            .withUsername("root")
            .withPassword("")
            .withReuse(true);
    static {
        mySQLContainer.start();
    }

} 