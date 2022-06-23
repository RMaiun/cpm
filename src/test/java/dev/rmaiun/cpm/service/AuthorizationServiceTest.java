package dev.rmaiun.cpm.service;

import static dev.rmaiun.cpm.dto.DomainAuthorizationType.R;
import static dev.rmaiun.cpm.dto.DomainAuthorizationType.RW;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.rmaiun.cpm.config.TestContainersSetup;
import dev.rmaiun.cpm.dto.ApplicationConfigurationDto;
import dev.rmaiun.cpm.dto.AuthorizeUserDto;
import dev.rmaiun.cpm.dto.DomainConfigurationDto;
import dev.rmaiun.cpm.dto.RegisterAppDtoIn;
import dev.rmaiun.cpm.exception.BusinessException;
import dev.rmaiun.cpm.exception.UserHasNoRightsException;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthorizationServiceTest extends TestContainersSetup {

  @Autowired private RegisterAppService registerAppService;

  @Autowired private ApplicationConfigurationService applicationConfigurationService;

  @Autowired private AuthorizationService authorizationService;

  @Test
  @DisplayName("Successful authorization for user")
  public void userIsSuccessfullyAuthorized() {
    registerAppService.registerApp(new RegisterAppDtoIn("app#1", "user#0"));
    var directConfigurations = Map.of("UserManagers", RW);
    var domains =
        List.of(
            new DomainConfigurationDto("abl", null, null, Map.of("UserManagers", RW)),
            new DomainConfigurationDto("infra", null, null, Map.of("InfraAdmins", RW)));
    var cfgDto =
        new ApplicationConfigurationDto(
            "app#1",
            true,
            domains,
            Map.of("UserManagers", List.of("user#1"), "InfraAdmins", List.of("user#1", "user#2")));
    var emptyDto = applicationConfigurationService.processAppConfig("user#0", cfgDto);
    assertNotNull(emptyDto);
    var authDto = new AuthorizeUserDto("app#1", "abl", "user#1", R);
    var result = authorizationService.authorizeUser("user#0", authDto);
    assertNotNull(result);
    assertTrue(result.success());
    assertNotNull(result.userAssignedGroups());
    assertEquals(1, result.userAssignedGroups().size());
    assertThat(result.userAssignedGroups(), hasItem("UserManagers"));
  }

  @Test
  @DisplayName("Failed authorization for user")
  public void failedToAuthorizeUser() {
    registerAppService.registerApp(new RegisterAppDtoIn("app#1", "user#0"));
    var directConfigurations = Map.of("UserManagers", RW);
    var domains =
        List.of(
            new DomainConfigurationDto("abl", null, null, Map.of("UserManagers", RW)),
            new DomainConfigurationDto("infra", null, null, Map.of("InfraAdmins", RW)));
    var cfgDto =
        new ApplicationConfigurationDto(
            "app#1",
            true,
            domains,
            Map.of("UserManagers", List.of("user#1"), "InfraAdmins", List.of("user#1", "user#2")));
    var emptyDto = applicationConfigurationService.processAppConfig("user#0", cfgDto);
    assertNotNull(emptyDto);
    var authDto = new AuthorizeUserDto("app#1", "abl", "user#2", R);
    var result = authorizationService.authorizeUser("user#0", authDto);
    assertNotNull(result);
    assertFalse(result.success());
    assertTrue(result.userAssignedGroups().isEmpty());
  }

  @Test
  @DisplayName("Not enough of rights for authorization")
  public void notEnoughRights() {
    registerAppService.registerApp(new RegisterAppDtoIn("app#1", "user#0"));
    var directConfigurations = Map.of("UserManagers", RW);
    var domains =
        List.of(
            new DomainConfigurationDto("abl", null, null, Map.of("UserManagers", RW)),
            new DomainConfigurationDto("infra", null, null, Map.of("InfraAdmins", RW)));
    var cfgDto =
        new ApplicationConfigurationDto(
            "app#1",
            true,
            domains,
            Map.of("UserManagers", List.of("user#1"), "InfraAdmins", List.of("user#1", "user#2")));
    var emptyDto = applicationConfigurationService.processAppConfig("user#0", cfgDto);
    assertNotNull(emptyDto);
    var authDto = new AuthorizeUserDto("app#1", "abl", "user#2", R);
    BusinessException err =
        assertThrows(
            BusinessException.class, () -> authorizationService.authorizeUser("user#3", authDto));
    assertEquals(UserHasNoRightsException.CODE, err.getCode());
  }
}
