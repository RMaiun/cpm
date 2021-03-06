package dev.rmaiun.cpm.service;

import static dev.rmaiun.cpm.TestAppRegistrar.CPM_TEST_APP_DEFAULT_USER1;
import static dev.rmaiun.cpm.TestAppRegistrar.CPM_TEST_APP_OWNER;
import static dev.rmaiun.cpm.TestAppRegistrar.CPM_TEST_GROUP;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.rmaiun.cpm.TestAppRegistrar;
import dev.rmaiun.cpm.config.TestContainersSetup;
import dev.rmaiun.cpm.dto.AssignUserToGroupDto;
import dev.rmaiun.cpm.exception.BusinessException;
import dev.rmaiun.cpm.exception.UserHasNoRightsException;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AssignUserToGroupServiceTest extends TestContainersSetup {

  public static final String USER_X = "userX";

  @Autowired private UserGroupService userGroupService;

  @Autowired private TestAppRegistrar testAppRegistrar;

  @Autowired private AssignUserToGroupService assignUserToGroupService;

  @Test
  @DisplayName("User is successfully assigned to group")
  public void userAssignedToGroupHds() {
    var app = testAppRegistrar.registerTestApp();
    var result =
        assignUserToGroupService.assignUserToGroups(
            CPM_TEST_APP_OWNER,
            new AssignUserToGroupDto(app.code(), USER_X, Set.of(CPM_TEST_GROUP)));
    assertNotNull(result);
    assertEquals(app.code(), result.app());
    assertEquals(0, result.existedAssignments().size());
    var userIsCorrectlyAssignedToGroup =
        userGroupService.checkUserAssignedToGroup(USER_X, app.code(), CPM_TEST_GROUP);
    assertTrue(userIsCorrectlyAssignedToGroup);
  }

  @Test
  @DisplayName("User is already assigned to group")
  public void userIsAlreadyAssignedToGroup() {
    var app = testAppRegistrar.registerTestApp();
    var result =
        assignUserToGroupService.assignUserToGroups(
            CPM_TEST_APP_OWNER,
            new AssignUserToGroupDto(
                app.code(), CPM_TEST_APP_DEFAULT_USER1, Set.of(CPM_TEST_GROUP)));
    assertNotNull(result);
    assertEquals(app.code(), result.app());
    assertEquals(1, result.existedAssignments().size());
    var userIsCorrectlyAssignedToGroup =
        userGroupService.checkUserAssignedToGroup(
            CPM_TEST_APP_DEFAULT_USER1, app.code(), CPM_TEST_GROUP);
    assertTrue(userIsCorrectlyAssignedToGroup);
  }

  @Test
  @DisplayName("User is not authorized for user-group assignment")
  public void userIsNotAuthorized() {
    var app = testAppRegistrar.registerTestApp();
    BusinessException err =
        assertThrows(
            BusinessException.class,
            () ->
                assignUserToGroupService.assignUserToGroups(
                    "userY",
                    new AssignUserToGroupDto(
                        app.code(), CPM_TEST_APP_DEFAULT_USER1, Set.of(CPM_TEST_GROUP))));
    assertEquals(UserHasNoRightsException.CODE, err.getCode());
  }
}
