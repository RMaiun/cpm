package dev.rmaiun.cpm.service;

import dev.rmaiun.cpm.dto.ApplicationConfigurationDto;
import dev.rmaiun.cpm.dto.EmptyDto;
import dev.rmaiun.cpm.dto.GroupRoleDto;
import dev.rmaiun.cpm.exception.AppNotFoundException;
import dev.rmaiun.cpm.exception.UserHasNoRightsException;
import dev.rmaiun.cpm.repository.ApplicationRepository;
import dev.rmaiun.cpm.utils.Constants;
import dev.rmaiun.cpm.utils.RoleTypeMapper;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ApplicationConfigurationService {

    private final ApplicationRepository applicationRepository;
    private final DomainService domainService;
    private final UserGroupService userGroupService;
    private final AppDataCleaner appDataCleaner;

    public ApplicationConfigurationService(
            ApplicationRepository applicationRepository,
            DomainService domainService,
            UserGroupService userGroupService,
            AppDataCleaner appDataCleaner) {
        this.applicationRepository = applicationRepository;
        this.domainService = domainService;
        this.userGroupService = userGroupService;
        this.appDataCleaner = appDataCleaner;
    }

    @Transactional
    public EmptyDto processAppConfig(String user, ApplicationConfigurationDto dto) {
        // check if app is present
        var app = applicationRepository
                .getByCode(dto.appCode())
                .orElseThrow(() -> new AppNotFoundException(dto.appCode()));
        // check that user can create app config
        boolean userCanWrite = userGroupService.checkUserCanWriteToDomain(dto.appCode(), Constants.ROOT_DOMAIN, user);
        if (!userCanWrite) {
            var msg = String.format("User %s has no rights to clean app data", user);
            throw new UserHasNoRightsException(msg);
        }
        // clean app configurations
        appDataCleaner.cleanAppData(app);
        // upsert missed domains which are available in configuration
        domainService.storeDomainConfigurations(app, dto.domains());
        var groupDtos = dto.domains().stream()
                .flatMap(domCfg -> domCfg.directConfiguration().entrySet().stream()
                        .map(e -> new GroupRoleDto(
                                e.getKey(), domCfg.code(), RoleTypeMapper.authRoleToDomainTypes(e.getValue()))))
                .toList();
        userGroupService.createMissingGroups(app, groupDtos);
        if (MapUtils.isNotEmpty(dto.relations())) {
            userGroupService.assignUserToGroup(app, dto.relations());
        }
        return new EmptyDto();
    }
}
