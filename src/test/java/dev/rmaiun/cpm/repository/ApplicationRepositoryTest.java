package dev.rmaiun.cpm.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.rmaiun.cpm.config.TestContainersSetup;
import dev.rmaiun.cpm.doman.Application;
import java.util.Objects;
import java.util.Optional;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql(scripts = {
    "/db/migration/V1__create_schema_cpm.sql",
    "/db/migration/V2__create_app_table.sql"
}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class ApplicationRepositoryTest extends TestContainersSetup {

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
  @DisplayName("Application is successfully created")
  public void storeAppSuccessfully() {
    var app = new Application(null, "testApp");
    long id = applicationRepository.save(app);
    assertTrue(id > 0);
    Optional<Application> found = applicationRepository.getById(id);
    assertTrue(found.isPresent());
    assertEquals(app.code(), found.get().code());
  }

  @Test
  @DisplayName("Application is not found")
  public void appNotFound() {
    var app = new Application(null, "testApp");
    applicationRepository.save(app);
    Optional<Application> found = applicationRepository.getById(34L);
    assertTrue(found.isEmpty());
  }

  @Test
  @DisplayName("Application is successfully deleted")
  public void deleteExisted() {
    var app = new Application(null, "testApp");
    long id = applicationRepository.save(app);
    Optional<Application> found = applicationRepository.getById(id);
    assertTrue(found.isPresent());
    var deletedElements = applicationRepository.delete(id);
    assertEquals(1, deletedElements);
    var all = applicationRepository.listAll();
    assertTrue(Objects.nonNull(all));
    assertTrue(all.isEmpty());
  }

  @Test
  @DisplayName("Application is not deleted")
  public void skipDeleteExisted() {
    var app = new Application(null, "testApp");
    long id = applicationRepository.save(app);
    Optional<Application> found = applicationRepository.getById(id);
    assertTrue(found.isPresent());
    var deletedElements = applicationRepository.delete(34L);
    assertEquals(0, deletedElements);
    var all = applicationRepository.listAll();
    assertTrue(Objects.nonNull(all));
    assertThat(all, hasSize(1));
  }
}
