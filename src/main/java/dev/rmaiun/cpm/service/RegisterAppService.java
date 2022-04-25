package dev.rmaiun.cpm.service;

import static dev.rmaiun.cpm.utils.Constants.APP_MANAGERS_GROUP;
import static dev.rmaiun.cpm.utils.Constants.APP_OWNERS_GROUP;
import static dev.rmaiun.cpm.utils.Constants.ROOT_DOMAIN;

import dev.rmaiun.cpm.doman.Application;
import dev.rmaiun.cpm.doman.BusinessGroup;
import dev.rmaiun.cpm.doman.BusinessRole;
import dev.rmaiun.cpm.doman.Domain;
import dev.rmaiun.cpm.doman.GroupRoleRelation;
import dev.rmaiun.cpm.doman.RoleType;
import dev.rmaiun.cpm.doman.UserGroupRelation;
import dev.rmaiun.cpm.dto.RegisterAppDtoIn;
import dev.rmaiun.cpm.dto.RegisterAppDtoOut;
import dev.rmaiun.cpm.exception.AppAlreadyExistsException;
import dev.rmaiun.cpm.repository.ApplicationRepository;
import dev.rmaiun.cpm.repository.BusinessGroupRepository;
import dev.rmaiun.cpm.repository.BusinessRoleRepository;
import dev.rmaiun.cpm.repository.DomainRepository;
import dev.rmaiun.cpm.repository.GroupRoleRepository;
import dev.rmaiun.cpm.repository.UserGroupRelationRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterAppService {

  private final ApplicationRepository appRepo;
  private final BusinessGroupRepository businessGroupRepo;
  private final BusinessRoleRepository businessRoleRepo;
  private final DomainRepository domainRepo;
  private final GroupRoleRepository groupRoleRepo;
  private final UserGroupRelationRepository userGroupRelationRepo;

  public RegisterAppService(ApplicationRepository appRepo, BusinessGroupRepository businessGroupRepo, BusinessRoleRepository businessRoleRepo, DomainRepository domainRepo,
      GroupRoleRepository groupRoleRepo, UserGroupRelationRepository userGroupRelationRepo) {
    this.appRepo = appRepo;
    this.businessGroupRepo = businessGroupRepo;
    this.businessRoleRepo = businessRoleRepo;
    this.domainRepo = domainRepo;
    this.groupRoleRepo = groupRoleRepo;
    this.userGroupRelationRepo = userGroupRelationRepo;
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
    var managerWriter = new GroupRoleRelation(managersGroupId, writerId);
    groupRoleRepo.batchSave(List.of(managerReader, managerWriter));
    //assign owner to related group
    var userOwnerGroup = new UserGroupRelation(dto.owner(), ownersGroupId);
    var userManagerGroup = new UserGroupRelation(dto.owner(), managersGroupId);
    userGroupRelationRepo.batchSave(List.of(userOwnerGroup, userManagerGroup));
    return new RegisterAppDtoOut(appId, dto.app(), List.of(dto.owner()));
  }
}
