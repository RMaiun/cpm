package dev.rmaiun.cpm.config;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@ContextConfiguration(initializers = {TestContainersSetup.Initializer.class})
@Testcontainers
@DirtiesContext
public class TestContainersSetup {

    @Container
    public static PostgreSQLContainer<?> postgresDB = new PostgreSQLContainer<>("postgres:10.5")
            .withDatabaseName("postgres")
            .withUsername("postgres")
            .withPassword("postgres")
            .withUrlParam("currentSchema", "cpm");

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresDB::getJdbcUrl);
        registry.add("spring.datasource.username", postgresDB::getUsername);
        registry.add("spring.datasource.password", postgresDB::getPassword);
    }

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of(
                            "spring.datasource.url=" + postgresDB.getJdbcUrl(),
                            "spring.datasource.username=" + postgresDB.getUsername(),
                            "spring.datasource.password=" + postgresDB.getPassword())
                    .applyTo(applicationContext.getEnvironment());
        }
    }
}
