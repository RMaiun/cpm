package dev.rmaiun.cpm.service;

import dev.rmaiun.cpm.doman.BusinessGroup;
import dev.rmaiun.cpm.dto.AuthorizationResultDto;
import dev.rmaiun.cpm.dto.AuthorizeUserDto;
import dev.rmaiun.cpm.exception.UserHasNoRightsException;
import dev.rmaiun.cpm.utils.Constants;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

  private final UserGroupService userGroupService;

  public AuthorizationService(UserGroupService userGroupService) {
    this.userGroupService = userGroupService;
  }

  public AuthorizationResultDto authorizeUser(String user, AuthorizeUserDto dto) {
    var userCanPerformAuth =
        userGroupService.checkUserAssignedToGroup(user, dto.app(), Constants.APP_MANAGERS_GROUP);
    if (!userCanPerformAuth) {
      var msg = String.format("User %s can't perform authorization actions", user);
      throw new UserHasNoRightsException(msg);
    }
    var groups =
        userGroupService
            .findGroupsForUserByDomain(dto.app(), dto.domain(), dto.authType(), dto.user())
            .stream()
            .map(BusinessGroup::code)
            .distinct()
            .collect(Collectors.toList());
    return CollectionUtils.isNotEmpty(groups)
        ? AuthorizationResultDto.success(groups)
        : AuthorizationResultDto.failure();
  }
}
