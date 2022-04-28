package dev.rmaiun.cpm.helper;

import dev.rmaiun.cpm.doman.RoleType;
import dev.rmaiun.cpm.dto.ApplicationConfigurationDto;
import dev.rmaiun.cpm.dto.EmptyDto;
import dev.rmaiun.cpm.dto.GroupRoleDto;
import dev.rmaiun.cpm.exception.AppNotFoundException;
import dev.rmaiun.cpm.exception.UserHasNoRightsException;
import dev.rmaiun.cpm.repository.ApplicationRepository;
import dev.rmaiun.cpm.service.DomainService;
import dev.rmaiun.cpm.service.UserGroupService;
import dev.rmaiun.cpm.utils.Constants;
import java.util.Collections;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ApplicationConfigurationHelper {

  private final ApplicationRepository applicationRepository;
  private final DomainService domainService;
  private final UserGroupService userGroupService;
  private final AppDataCleaner appDataCleaner;

  public ApplicationConfigurationHelper(ApplicationRepository applicationRepository, DomainService domainService, UserGroupService userGroupService, AppDataCleaner appDataCleaner) {
    this.applicationRepository = applicationRepository;
    this.domainService = domainService;
    this.userGroupService = userGroupService;
    this.appDataCleaner = appDataCleaner;
  }

  @Transactional
  public EmptyDto processAppConfig(String user, ApplicationConfigurationDto dto) {
    // check if app is present
    var app = applicationRepository.getByCode(dto.appCode())
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
            .map(e -> new GroupRoleDto(e.getKey(), domCfg.code(), roleType(e.getValue()))))
        .toList();
    userGroupService.createMissingGroups(app, groupDtos);
    userGroupService.assignUserToGroup(app, dto.relations());
    return new EmptyDto();
  }

  private Set<RoleType> roleType(String str) {
    return switch (str) {
      case "R" -> Set.of(RoleType.Reader);
      case "W" -> Set.of(RoleType.Writer);
      case "RW" -> Set.of(RoleType.Reader, RoleType.Writer);
      default -> Collections.emptySet();
    };
  }
}
