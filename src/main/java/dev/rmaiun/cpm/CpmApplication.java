package dev.rmaiun.cpm;

import dev.rmaiun.cpm.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CpmApplication {

  @Autowired
  private ApplicationRepository appRepo;
  // @Autowired
  // private DomainRepository domainObjectRepo;
  // @Autowired
  // private BusinessRoleRepository businessRoleRepo;
  //
  // @Autowired
  // private GroupRepository groupRepository;

  public static void main(String[] args) {
    SpringApplication.run(CpmApplication.class, args);
  }

}
