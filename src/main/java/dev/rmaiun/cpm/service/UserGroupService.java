package dev.rmaiun.cpm.service;

import static dev.rmaiun.cpm.doman.RoleType.WRITER;
import static java.util.Objects.isNull;

import dev.rmaiun.cpm.doman.Application;
import dev.rmaiun.cpm.doman.BusinessGroup;
import dev.rmaiun.cpm.doman.GroupRoleRelation;
import dev.rmaiun.cpm.doman.UserGroupRelation;
import dev.rmaiun.cpm.dto.DomainAuthorizationType;
import dev.rmaiun.cpm.dto.GroupRoleDto;
import dev.rmaiun.cpm.exception.GroupNotFoundException;
import dev.rmaiun.cpm.repository.GroupRepository;
import dev.rmaiun.cpm.repository.GroupRoleRepository;
import dev.rmaiun.cpm.repository.RoleRepository;
import dev.rmaiun.cpm.repository.UserGroupRelationRepository;
import dev.rmaiun.cpm.utils.RoleTypeMapper;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

@Service
public class UserGroupService {

    private final GroupRoleRepository groupRoleRepository;
    private final UserGroupRelationRepository userGroupRelationRepository;
    private final GroupRepository groupRepository;
    private final RoleRepository roleRepository;

    public UserGroupService(
            GroupRoleRepository groupRoleRepository,
            UserGroupRelationRepository userGroupRelationRepository,
            GroupRepository groupRepository,
            RoleRepository roleRepository) {
        this.groupRoleRepository = groupRoleRepository;
        this.userGroupRelationRepository = userGroupRelationRepository;
        this.groupRepository = groupRepository;
        this.roleRepository = roleRepository;
    }

    public boolean checkUserCanWriteToDomain(String app, String domain, String user) {
        var foundGroups = groupRepository.listGroupAssignedToDomainByUserRole(app, domain, Set.of(WRITER), user);
        return CollectionUtils.isNotEmpty(foundGroups);
    }

    public void createMissingGroups(Application app, List<GroupRoleDto> groupRoles) {
        groupRoles.forEach(dto -> {
            var foundGroup =
                    groupRepository.findByAppCode(dto.group(), app.code()).orElseGet(() -> createGroup(app, dto));
            bindGroupToDomainRoles(app, dto, foundGroup);
        });
    }

    public void assignUserToGroup(Application app, Map<String, List<String>> groupUsers) {
        var appGroups = groupRepository.listByApp(app.code()).stream()
                .collect(Collectors.toMap(BusinessGroup::code, BusinessGroup::id));
        var absentGroups = groupUsers.keySet().stream()
                .filter(g -> isNull(appGroups.get(g)))
                .collect(Collectors.toSet());
        if (!absentGroups.isEmpty()) {
            throw new GroupNotFoundException(app.code(), String.join(",", absentGroups));
        }
        var userGroupRelations = groupUsers.entrySet().stream()
                .flatMap(e -> e.getValue().stream().map(user -> new UserGroupRelation(user, appGroups.get(e.getKey()))))
                .collect(Collectors.toSet());
        userGroupRelationRepository.batchSave(userGroupRelations);
    }

    public List<BusinessGroup> findGroupsForUserByDomain(
            String app, String domain, DomainAuthorizationType type, String user) {
        var roleTypes = RoleTypeMapper.authRoleToDomainTypes(type);
        return groupRepository.listGroupAssignedToDomainByUserRole(app, domain, roleTypes, user);
    }

    public boolean checkUserAssignedToGroup(String user, String app, String group) {
        var foundGroups = groupRepository.listAssignedGroupsForUserByApp(user, app);
        return foundGroups.stream().anyMatch(g -> g.code().equals(group));
    }

    private void bindGroupToDomainRoles(Application app, GroupRoleDto dto, BusinessGroup group) {
        var domainRoles = roleRepository.findRolesByAppDomain(app.code(), dto.domain());
        domainRoles.stream()
                .filter(role -> dto.roleTypes().contains(role.type()))
                .forEach(role -> groupRoleRepository.saveWithoutId(new GroupRoleRelation(group.id(), role.id())));
    }

    private BusinessGroup createGroup(Application app, GroupRoleDto dto) {
        var gid = groupRepository.save(new BusinessGroup(0L, dto.group(), app.id()));
        return new BusinessGroup(gid, dto.group(), app.id());
    }
}
