package dev.rmaiun.cpm.repository;

import dev.rmaiun.cpm.doman.Application;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(initializers = {ApplicationRepositoryTest.Initializer.class})
@Testcontainers
@DirtiesContext
@Sql(scripts = {
    "/db/migration/V1__create_schema_cpm.sql",
    "/db/migration/V2__create_app_table.sql"
}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class ApplicationRepositoryTest {

  @Container
  public static PostgreSQLContainer<?> postgresDB = new PostgreSQLContainer<>("postgres:10.5")
      .withDatabaseName("postgres")
      .withUsername("postgres")
      .withPassword("postgres")
      .withUrlParam("currentSchema", "cpm");

  @DynamicPropertySource
  public static void properties(DynamicPropertyRegistry registry) {
    System.out.println("!!!!!!!!! " + postgresDB.getJdbcUrl());
    registry.add("spring.datasource.url", postgresDB::getJdbcUrl);
    registry.add("spring.datasource.username", postgresDB::getUsername);
    registry.add("spring.datasource.password", postgresDB::getPassword);

  }

  static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
      TestPropertyValues.of(
          "spring.datasource.url=" + postgresDB.getJdbcUrl(),
          "spring.datasource.username=" + postgresDB.getUsername(),
          "spring.datasource.password=" + postgresDB.getPassword()
      ).applyTo(applicationContext.getEnvironment());
    }
  }

  @Autowired
  private ApplicationRepository applicationRepository;
  @Autowired
  private NamedParameterJdbcTemplate jdbcTemplate;

  @Before
  @After
  public void cleanWorkspace() {
    applicationRepository.clearTable();

  }

  @Test
  public void storeAppSuccessfully() {
    var app = new Application(null, "testApp");
    long id = applicationRepository.save(app);
    Assertions.assertTrue(id > 0);
  }
}
