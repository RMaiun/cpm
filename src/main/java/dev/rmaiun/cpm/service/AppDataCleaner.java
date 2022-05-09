package dev.rmaiun.cpm.service;

import dev.rmaiun.cpm.doman.Application;
import dev.rmaiun.cpm.doman.BusinessGroup;
import dev.rmaiun.cpm.doman.BusinessRole;
import dev.rmaiun.cpm.doman.Domain;
import dev.rmaiun.cpm.doman.DomainToDomain;
import dev.rmaiun.cpm.repository.DomainRepository;
import dev.rmaiun.cpm.repository.DomainToDomainRepository;
import dev.rmaiun.cpm.repository.GroupRepository;
import dev.rmaiun.cpm.repository.GroupRoleRepository;
import dev.rmaiun.cpm.repository.RoleRepository;
import dev.rmaiun.cpm.repository.UserGroupRelationRepository;
import dev.rmaiun.cpm.utils.Constants;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppDataCleaner {

    private final DomainRepository domainRepository;
    private final DomainToDomainRepository domain2domainRepository;
    private final GroupRepository groupRepository;
    private final GroupRoleRepository groupRoleRepository;
    private final RoleRepository roleRepository;
    private final UserGroupRelationRepository userGroupRelationRepository;

    public AppDataCleaner(
            DomainRepository domainRepository,
            DomainToDomainRepository domain2domainRepository,
            GroupRepository groupRepository,
            GroupRoleRepository groupRoleRepository,
            RoleRepository roleRepository,
            UserGroupRelationRepository userGroupRelationRepository) {
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
                .filter(domain -> !domain.code().equals(Constants.ROOT_DOMAIN))
                .map(Domain::id)
                .toList();
        var domainToDomains = domain2domainRepository.listByDomains(domains).stream()
                .map(DomainToDomain::id)
                .toList();
        var groups = groupRepository.listByApp(app.code()).stream()
                .filter(g -> !List.of(Constants.APP_MANAGERS_GROUP, Constants.APP_OWNERS_GROUP)
                        .contains(g.code()))
                .map(BusinessGroup::id)
                .toList();
        var roles = roleRepository.findRolesByApp(app.id()).stream()
                .filter(br -> domains.contains(br.domainId()))
                .map(BusinessRole::id)
                .toList();
        groupRoleRepository.deleteByGroupIds(groups);
        userGroupRelationRepository.deleteByGroupIds(groups);
        groupRepository.delete(groups);
        roleRepository.delete(roles);
        domain2domainRepository.delete(domainToDomains);
        domainRepository.delete(domains);
    }
}
