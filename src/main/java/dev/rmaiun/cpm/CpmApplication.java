package dev.rmaiun.cpm;

import dev.rmaiun.cpm.doman.Application;
import dev.rmaiun.cpm.doman.BusinessRole;
import dev.rmaiun.cpm.doman.DomainObject;
import dev.rmaiun.cpm.repository.ApplicationRepository;
import dev.rmaiun.cpm.repository.BusinessRoleRepository;
import dev.rmaiun.cpm.repository.DomainObjectRepository;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CpmApplication implements CommandLineRunner {

  @Autowired
  private ApplicationRepository appRepo;
  @Autowired
  private DomainObjectRepository domainObjectRepo;
  @Autowired
  private BusinessRoleRepository businessRoleRepo;

  public static void main(String[] args) {
    SpringApplication.run(CpmApplication.class, args);
  }

  @Override
  @Transactional
  public void run(String... args) throws Exception {
    Application app = appRepo.save(new Application(null, "someapp"));
    var object = domainObjectRepo.save(new DomainObject(null, "someService", app));
    var br1 = new BusinessRole(null, "br1", null, object);
    var br2 = new BusinessRole(null, "br2", null, object);
    businessRoleRepo.save(br1);
    businessRoleRepo.save(br2);
  }
}
