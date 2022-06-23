package dev.rmaiun.cpm.service;

import static dev.rmaiun.cpm.utils.Constants.APP_MANAGERS_GROUP;
import static dev.rmaiun.cpm.utils.Constants.APP_OWNERS_GROUP;
import static dev.rmaiun.cpm.utils.Constants.ROOT_DOMAIN;

import dev.rmaiun.cpm.doman.Application;
import dev.rmaiun.cpm.doman.BusinessGroup;
import dev.rmaiun.cpm.doman.BusinessRole;
import dev.rmaiun.cpm.doman.Domain;
import dev.rmaiun.cpm.doman.DomainToDomain;
import dev.rmaiun.cpm.doman.GroupRoleRelation;
import dev.rmaiun.cpm.doman.RoleType;
import dev.rmaiun.cpm.doman.UserGroupRelation;
import dev.rmaiun.cpm.dto.RegisterAppDtoIn;
import dev.rmaiun.cpm.dto.RegisterAppDtoOut;
import dev.rmaiun.cpm.exception.AppAlreadyExistsException;
import dev.rmaiun.cpm.repository.ApplicationRepository;
import dev.rmaiun.cpm.repository.DomainRepository;
import dev.rmaiun.cpm.repository.DomainToDomainRepository;
import dev.rmaiun.cpm.repository.GroupRepository;
import dev.rmaiun.cpm.repository.GroupRoleRepository;
import dev.rmaiun.cpm.repository.RoleRepository;
import dev.rmaiun.cpm.repository.UserGroupRelationRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterAppService {

  private final ApplicationRepository appRepo;
  private final GroupRepository businessGroupRepo;
  private final RoleRepository businessRoleRepo;
  private final DomainRepository domainRepo;
  private final GroupRoleRepository groupRoleRepo;
  private final UserGroupRelationRepository userGroupRelationRepo;
  private final DomainToDomainRepository domainToDomainRepo;

  public RegisterAppService(
      ApplicationRepository appRepo,
      GroupRepository businessGroupRepo,
      RoleRepository businessRoleRepo,
      DomainRepository domainRepo,
      GroupRoleRepository groupRoleRepo,
      UserGroupRelationRepository userGroupRelationRepo,
      DomainToDomainRepository groupToGroupRelationRepo,
      DomainToDomainRepository domainToDomainRepo) {
    this.appRepo = appRepo;
    this.businessGroupRepo = businessGroupRepo;
    this.businessRoleRepo = businessRoleRepo;
    this.domainRepo = domainRepo;
    this.groupRoleRepo = groupRoleRepo;
    this.userGroupRelationRepo = userGroupRelationRepo;
    this.domainToDomainRepo = domainToDomainRepo;
  }

  @Transactional
  public RegisterAppDtoOut registerApp(RegisterAppDtoIn dto) {
    // check app exists
    var app = appRepo.getByCode(dto.app());
    if (app.isPresent()) {
      throw new AppAlreadyExistsException(dto.app());
    }
    // create app
    var appToCreate = new Application(0L, dto.app());
    var appId = appRepo.save(appToCreate);
    // create default domain
    var domain = new Domain(0L, ROOT_DOMAIN, appId);
    var domId = domainRepo.save(domain);
    // create default domain business roles
    var reader = new BusinessRole(0L, domId, RoleType.READER);
    var readerId = businessRoleRepo.save(reader);
    var writer = new BusinessRole(0L, domId, RoleType.WRITER);
    var writerId = businessRoleRepo.save(writer);
    // create default groups
    var ownersGroup = new BusinessGroup(0L, APP_OWNERS_GROUP, appId);
    var managersGroup = new BusinessGroup(0L, APP_MANAGERS_GROUP, appId);
    var ownersGroupId = businessGroupRepo.save(ownersGroup);
    var managersGroupId = businessGroupRepo.save(managersGroup);
    // assign roles to group
    var managerReader = new GroupRoleRelation(managersGroupId, readerId);
    var managerWRITER = new GroupRoleRelation(managersGroupId, writerId);
    groupRoleRepo.batchSave(List.of(managerReader, managerWRITER));
    // assign owner to related group
    var userOwnerGroup = new UserGroupRelation(dto.owner(), ownersGroupId);
    var userManagerGroup = new UserGroupRelation(dto.owner(), managersGroupId);
    userGroupRelationRepo.batchSave(List.of(userOwnerGroup, userManagerGroup));
    // create default domain relation
    var d2d = new DomainToDomain(0L, domId, null);
    domainToDomainRepo.save(d2d);
    // return result
    return new RegisterAppDtoOut(appId, dto.app(), List.of(dto.owner()));
  }
}
