package dev.rmaiun.cpm;

import dev.rmaiun.cpm.doman.Application;
import dev.rmaiun.cpm.doman.BusinessRole;
import dev.rmaiun.cpm.doman.DomainObject;
import dev.rmaiun.cpm.repository.ApplicationRepository;
import dev.rmaiun.cpm.repository.BusinessRoleRepository;
import dev.rmaiun.cpm.repository.DomainObjectRepository;
import java.util.List;
import java.util.stream.Collectors;
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
    // Application app = appRepo.save(new Application("someapp"));
    // System.out.println("app");
    // var object = domainObjectRepo.save(new DomainObject("someService", app));
    // System.out.println("object");
    // var br1 = new BusinessRole("br1", null, object);
    // var br2 = new BusinessRole("br2", null, object);
    // businessRoleRepo.saveAll(List.of(br1, br2));
    // System.out.println("br1, br2");
    // var br3 = new BusinessRole("br3", null, object);
    // br3.getChildren().add(br1);
    // businessRoleRepo.save(br3);
    // System.out.println("br3");
    // br1.getChildren().add(new BusinessRole("br1_1",null, object));
    // businessRoleRepo.save(br1);
    var byId = businessRoleRepo.findByParent(47L);
    System.out.println(byId);
  }
}
