package dev.rmaiun.cpm.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import dev.rmaiun.cpm.config.TestContainersSetup;
import dev.rmaiun.cpm.doman.Application;
import dev.rmaiun.cpm.dto.RegisterAppDtoIn;
import dev.rmaiun.cpm.exception.AppAlreadyExistsException;
import dev.rmaiun.cpm.exception.BusinessException;
import dev.rmaiun.cpm.repository.ApplicationRepository;
import dev.rmaiun.cpm.repository.GroupRepository;
import dev.rmaiun.cpm.repository.RoleRepository;
import dev.rmaiun.cpm.repository.DomainRepository;
import dev.rmaiun.cpm.repository.DomainToDomainRepository;
import dev.rmaiun.cpm.repository.GroupRoleRepository;
import dev.rmaiun.cpm.repository.UserGroupRelationRepository;
import dev.rmaiun.cpm.helper.RegisterAppHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
    "/db/migration/V2__create_app_table.sql",
    "/db/migration/V3__create_domain_table.sql",
    "/db/migration/V4__create_business_role_table.sql",
    "/db/migration/V5__create_business_group_table.sql",
    "/db/migration/V6__create_group_role_table.sql",
    "/db/migration/V7__create_user_group_table.sql",
    "/db/migration/V8__create_domain_to_domain_table.sql"
}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class RegisterAppHelperTest extends TestContainersSetup {

  @Autowired
  private ApplicationRepository applicationRepository;
  @Autowired
  private DomainRepository domainRepository;
  @Autowired
  private GroupRepository businessGroupRepo;
  @Autowired
  private RoleRepository businessRoleRepo;
  @Autowired
  private GroupRoleRepository groupRoleRepo;
  @Autowired
  private UserGroupRelationRepository userGroupRelationRepo;
  @Autowired
  private RegisterAppHelper registerAppHelper;
  @Autowired
  private DomainToDomainRepository domainToDomainRepo;
  @Autowired
  private NamedParameterJdbcTemplate jdbcTemplate;

  @BeforeEach
  @AfterEach
  @Sql(scripts = {"/db/scripts/V0__clean_env.sql"})
  public void setup() {
  }

  @Test
  @DisplayName("Application is already present")
  public void appAlreadyPresentError() {
    var app = new Application(1L, "testApp");
    applicationRepository.save(app);
    var dto = new RegisterAppDtoIn(app.code(), "someUser");
    BusinessException err = assertThrows(BusinessException.class, () -> registerAppHelper.registerApp(dto));
    assertEquals(AppAlreadyExistsException.CODE, err.getCode());
  }

  @Test
  @DisplayName("New Application is successfully registered")
  public void appAlreadyPresent() {
    var dto = new RegisterAppDtoIn("testApp", "someUser");
    var res = registerAppHelper.registerApp(dto);
    assertNotNull(res);
    assertEquals(dto.app(), res.code());
    var storedDomains = domainRepository.listAll();
    assertEquals(1, storedDomains.size());
    var storedRoles = businessRoleRepo.listAll();
    assertEquals(2, storedRoles.size());
    var storedGroups = businessGroupRepo.listAll();
    assertEquals(2, storedGroups.size());
    var storedGroupRoles = groupRoleRepo.listAll();
    assertEquals(2, storedGroupRoles.size());
    var storedUserGroups = userGroupRelationRepo.listAll();
    assertEquals(2, storedUserGroups.size());
    var domainToDomains = domainToDomainRepo.listAll();
    assertEquals(1, domainToDomains.size());
  }
}
