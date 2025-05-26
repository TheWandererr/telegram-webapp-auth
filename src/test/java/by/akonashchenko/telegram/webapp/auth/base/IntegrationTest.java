package by.akonashchenko.telegram.webapp.auth.base;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest extends BaseTest {

    @Container
    protected static PostgreSQLContainer<?> DB = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test")
            .withUsername("user")
            .withPassword("password");

    @Autowired
    protected TestRestTemplate restTemplate;

    @BeforeAll
    public static void setUp() {
        System.setProperty("spring.datasource.url", DB.getJdbcUrl());
        System.setProperty("spring.datasource.username", DB.getUsername());
        System.setProperty("spring.datasource.password", DB.getPassword());
    }
}
