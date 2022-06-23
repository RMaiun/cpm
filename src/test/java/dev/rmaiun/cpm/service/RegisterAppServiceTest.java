package dev.rmaiun.cpm.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import dev.rmaiun.cpm.config.TestContainersSetup;
import dev.rmaiun.cpm.doman.Application;
import dev.rmaiun.cpm.dto.RegisterAppDtoIn;
import dev.rmaiun.cpm.exception.AppAlreadyExistsException;
import dev.rmaiun.cpm.exception.BusinessException;
import dev.rmaiun.cpm.repository.ApplicationRepository;
import dev.rmaiun.cpm.repository.DomainRepository;
import dev.rmaiun.cpm.repository.DomainToDomainRepository;
import dev.rmaiun.cpm.repository.GroupRepository;
import dev.rmaiun.cpm.repository.GroupRoleRepository;
import dev.rmaiun.cpm.repository.RoleRepository;
import dev.rmaiun.cpm.repository.UserGroupRelationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RegisterAppServiceTest extends TestContainersSetup {

  @Autowired private ApplicationRepository applicationRepository;

  @Autowired private DomainRepository domainRepository;

  @Autowired private GroupRepository businessGroupRepo;

  @Autowired private RoleRepository businessRoleRepo;

  @Autowired private GroupRoleRepository groupRoleRepo;

  @Autowired private UserGroupRelationRepository userGroupRelationRepo;

  @Autowired private RegisterAppService registerAppService;

  @Autowired private DomainToDomainRepository domainToDomainRepo;

  @Autowired private NamedParameterJdbcTemplate jdbcTemplate;

  @Test
  @DisplayName("Application is already present")
  public void appAlreadyPresentError() {
    var app = new Application(1L, "testApp");
    applicationRepository.save(app);
    var dto = new RegisterAppDtoIn(app.code(), "someUser");
    BusinessException err =
        assertThrows(BusinessException.class, () -> registerAppService.registerApp(dto));
    assertEquals(AppAlreadyExistsException.CODE, err.getCode());
  }

  @Test
  @DisplayName("New Application is successfully registered")
  public void appAlreadyPresent() {
    var dto = new RegisterAppDtoIn("testApp", "someUser");
    var res = registerAppService.registerApp(dto);
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
