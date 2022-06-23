package dev.rmaiun.cpm.config;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@ContextConfiguration(initializers = {TestContainersSetup.Initializer.class})
@Testcontainers
@DirtiesContext
@Sql(
    scripts = {
      "/db/scripts/create_schema_cpm.sql",
      "/db/migration/V1__create_app_table.sql",
      "/db/migration/V2__create_domain_table.sql",
      "/db/migration/V3__create_business_role_table.sql",
      "/db/migration/V4__create_business_group_table.sql",
      "/db/migration/V5__create_group_role_table.sql",
      "/db/migration/V6__create_user_group_table.sql",
      "/db/migration/V7__create_domain_to_domain_table.sql"
    },
    executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class TestContainersSetup {

  @BeforeEach
  @AfterEach
  @Sql(scripts = {"/db/scripts/clean_env.sql"})
  public void setup() {}

  @Container
  public static PostgreSQLContainer<?> postgresDB =
      new PostgreSQLContainer<>("postgres:10.5")
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

  public static class Initializer
      implements ApplicationContextInitializer<ConfigurableApplicationContext> {

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
