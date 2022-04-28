package dev.rmaiun.cpm.helper;

import dev.rmaiun.cpm.doman.Application;
import dev.rmaiun.cpm.doman.BusinessGroup;
import dev.rmaiun.cpm.doman.BusinessRole;
import dev.rmaiun.cpm.doman.Domain;
import dev.rmaiun.cpm.doman.DomainToDomain;
import dev.rmaiun.cpm.repository.ApplicationRepository;
import dev.rmaiun.cpm.repository.DomainRepository;
import dev.rmaiun.cpm.repository.DomainToDomainRepository;
import dev.rmaiun.cpm.repository.GroupRepository;
import dev.rmaiun.cpm.repository.GroupRoleRepository;
import dev.rmaiun.cpm.repository.RoleRepository;
import dev.rmaiun.cpm.repository.UserGroupRelationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppDataCleaner {

  private final ApplicationRepository applicationRepository;
  private final DomainRepository domainRepository;
  private final DomainToDomainRepository domain2domainRepository;
  private final GroupRepository groupRepository;
  private final GroupRoleRepository groupRoleRepository;
  private final RoleRepository roleRepository;
  private final UserGroupRelationRepository userGroupRelationRepository;

  public AppDataCleaner(ApplicationRepository applicationRepository, DomainRepository domainRepository, DomainToDomainRepository domain2domainRepository, GroupRepository groupRepository,
      GroupRoleRepository groupRoleRepository, RoleRepository roleRepository, UserGroupRelationRepository userGroupRelationRepository) {
    this.applicationRepository = applicationRepository;
    this.domainRepository = domainRepository;
    this.domain2domainRepository = domain2domainRepository;
    this.groupRepository = groupRepository;
    this.groupRoleRepository = groupRoleRepository;
    this.roleRepository = roleRepository;
    this.userGroupRelationRepository = userGroupRelationRepository;
  }

  @Transactional
  public void cleanAppData(Application app) {
    var domains = domainRepository.listDomainByApplication(app.code()).stream()
        .map(Domain::id)
        .toList();
    var domainToDomains = domain2domainRepository.listByDomains(domains).stream()
        .map(DomainToDomain::id)
        .toList();
    var groups = groupRepository.listByApp(app.code()).stream()
        .map(BusinessGroup::id)
        .toList();
    var roles = roleRepository.findRolesByApp(app.id()).stream()
        .map(BusinessRole::id)
        .toList();
    groupRoleRepository.deleteByGroupIds(groups);
    userGroupRelationRepository.deleteByGroupIds(groups);
    domain2domainRepository.delete(domainToDomains);
    groupRepository.delete(groups);
    roleRepository.delete(roles);
    domainRepository.delete(domains);
  }
}
