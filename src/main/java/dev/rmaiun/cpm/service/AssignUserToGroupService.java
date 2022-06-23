package dev.rmaiun.cpm.service;

import dev.rmaiun.cpm.dto.AssignUserToGroupDto;
import dev.rmaiun.cpm.dto.UserGroupStatsDto;
import dev.rmaiun.cpm.exception.AppNotFoundException;
import dev.rmaiun.cpm.exception.UserHasNoRightsException;
import dev.rmaiun.cpm.repository.ApplicationRepository;
import dev.rmaiun.cpm.utils.Constants;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

@Service
public class AssignUserToGroupService {

  private final UserGroupService userGroupService;
  private final ApplicationRepository applicationRepository;

  public AssignUserToGroupService(
      UserGroupService userGroupService, ApplicationRepository applicationRepository) {
    this.userGroupService = userGroupService;
    this.applicationRepository = applicationRepository;
  }

  public UserGroupStatsDto assignUserToGroups(String user, AssignUserToGroupDto dto) {
    var userCanPerformAuth =
        userGroupService.checkUserAssignedToGroup(user, dto.app(), Constants.APP_MANAGERS_GROUP);
    if (!userCanPerformAuth) {
      var msg = String.format("User %s can't perform assignment actions", user);
      throw new UserHasNoRightsException(msg);
    }
    var app =
        applicationRepository
            .getByCode(dto.app())
            .orElseThrow(() -> new AppNotFoundException(dto.app()));
    var groupUsers =
        dto.groups().stream()
            .map(group -> Pair.of(group, List.of(dto.user())))
            .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    var userExistedGroupsMap = userGroupService.assignUserToGroup(app, groupUsers);
    List<String> existedGroups = userExistedGroupsMap.getOrDefault(dto.user(), new ArrayList<>());
    return new UserGroupStatsDto(dto.app(), dto.user(), existedGroups);
  }
}
