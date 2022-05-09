package dev.rmaiun.cpm.service;

import static java.util.Optional.ofNullable;

import dev.rmaiun.cpm.doman.Application;
import dev.rmaiun.cpm.doman.BusinessRole;
import dev.rmaiun.cpm.doman.Domain;
import dev.rmaiun.cpm.doman.DomainToDomain;
import dev.rmaiun.cpm.doman.RoleType;
import dev.rmaiun.cpm.dto.DomainConfigurationDto;
import dev.rmaiun.cpm.repository.DomainRepository;
import dev.rmaiun.cpm.repository.DomainToDomainRepository;
import dev.rmaiun.cpm.repository.RoleRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DomainService {

    private final DomainRepository domainRepository;
    private final RoleRepository roleRepository;
    private final DomainToDomainRepository d2dRepository;

    public DomainService(
            DomainRepository domainRepository, RoleRepository roleRepository, DomainToDomainRepository d2dRepository) {
        this.domainRepository = domainRepository;
        this.roleRepository = roleRepository;
        this.d2dRepository = d2dRepository;
    }

    public void storeDomainConfigurations(Application app, List<DomainConfigurationDto> configurations) {
        configurations.forEach(cfg -> storeConfiguration(app, cfg));
    }

    public void storeConfiguration(Application app, DomainConfigurationDto dto) {
        Long parentId = ofNullable(dto.parent()).map(p -> prepareDomain(p, app)).orElse(null);
        Long domId = prepareDomain(dto.code(), app);
        var d2d = new DomainToDomain(0L, domId, parentId);
        d2dRepository.save(d2d);
    }

    private Long prepareDomain(String domain, Application app) {
        return domainRepository
                .findDomainByApplication(domain, app.code())
                .map(Domain::id)
                .orElseGet(() -> {
                    var id = domainRepository.save(new Domain(0L, domain, app.id()));
                    roleRepository.save(new BusinessRole(0L, id, RoleType.READER));
                    roleRepository.save(new BusinessRole(0L, id, RoleType.WRITER));
                    return id;
                });
    }
}
