package dev.rmaiun.cpm.service;

import dev.rmaiun.cpm.doman.Application;
import dev.rmaiun.cpm.doman.BusinessGroup;
import dev.rmaiun.cpm.doman.GroupRoleRelation;
import dev.rmaiun.cpm.dto.GroupRoleDto;
import dev.rmaiun.cpm.repository.GroupRepository;
import dev.rmaiun.cpm.repository.GroupRoleRepository;
import dev.rmaiun.cpm.repository.RoleRepository;
import dev.rmaiun.cpm.repository.UserGroupRelationRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UserGroupService {

  private final GroupRoleRepository groupRoleRepository;
  private final UserGroupRelationRepository userGroupRelationRepository;
  private final GroupRepository groupRepository;
  private final RoleRepository roleRepository;

  public void createMissingGroups(Application app, List<GroupRoleDto> groupRoles) {
    groupRoles.forEach(dto -> {
      var foundGroup = groupRepository.findByDomainCode(dto.group(), dto.domain())
          .orElseGet(() -> createGroup(app, dto));
      bindGroupToDomainRoles(app, dto, foundGroup);
    });
  }

  private void bindGroupToDomainRoles(Application app, GroupRoleDto dto, BusinessGroup group) {
    var domainRoles = roleRepository.findRolesByAppDomain(app.code(), dto.domain());
    domainRoles.stream()
        .filter(role -> dto.roleTypes().contains(role.type()))
        .forEach(role -> groupRoleRepository.save(new GroupRoleRelation(group.id(), role.id())));
  }

  private BusinessGroup createGroup(Application app, GroupRoleDto dto) {
    var gid = groupRepository.save(new BusinessGroup(0L, dto.group(), app.id()));
    return new BusinessGroup(gid, dto.group(), app.id());
  }
}
