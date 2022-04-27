package dev.rmaiun.cpm.service;

import static java.util.Objects.nonNull;

import dev.rmaiun.cpm.doman.Application;
import dev.rmaiun.cpm.doman.BusinessRole;
import dev.rmaiun.cpm.doman.Domain;
import dev.rmaiun.cpm.doman.RoleType;
import dev.rmaiun.cpm.dto.DomainConfigurationDto;
import dev.rmaiun.cpm.repository.RoleRepository;
import dev.rmaiun.cpm.repository.DomainRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DomainService {

  private final DomainRepository domainRepository;
  private final RoleRepository roleRepository;

  public DomainService(DomainRepository domainRepository, RoleRepository roleRepository) {
    this.domainRepository = domainRepository;
    this.roleRepository = roleRepository;
  }

  public void storeDomainConfigurations(Application app, List<DomainConfigurationDto> configurations) {
    configurations.forEach(cfg -> storeConfiguration(app, cfg));
  }

  public void storeConfiguration(Application app, DomainConfigurationDto dto) {
    if (nonNull(dto.parent())) {
      var parent = domainRepository.findDomainByApplication(dto.parent(), app.code());
      if (parent.isEmpty()) {
        var parentId = domainRepository.save(new Domain(0L, dto.code(), app.id()));
        roleRepository.save(new BusinessRole(0L, parentId, RoleType.Reader));
        roleRepository.save(new BusinessRole(0L, parentId, RoleType.Writer));
      }
    }
    var foundDomain = domainRepository.findDomainByApplication(dto.code(), app.code());
    if (foundDomain.isEmpty()) {
      var domId = domainRepository.save(new Domain(0L, dto.code(), app.id()));
      roleRepository.save(new BusinessRole(0L, domId, RoleType.Reader));
      roleRepository.save(new BusinessRole(0L, domId, RoleType.Writer));
    }
  }
}
