package dev.rmaiun.cpm.service;

import dev.rmaiun.cpm.dto.GroupRoleDto;
import dev.rmaiun.cpm.repository.GroupRoleRepository;
import dev.rmaiun.cpm.repository.UserGroupRelationRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UserGroupService {

  private final GroupRoleRepository groupRoleRepository;
  private final UserGroupRelationRepository userGroupRelationRepository;

  public void createGroups(List<GroupRoleDto> groupRoles) {
    groupRoles.forEach(dto ->);
  }

  private void createGroup(GroupRoleDto dto) {
    dto.roleTypes()
  }
}
