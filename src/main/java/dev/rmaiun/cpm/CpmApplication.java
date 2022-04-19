package dev.rmaiun.cpm;

import dev.rmaiun.cpm.doman.Application;
import dev.rmaiun.cpm.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
public class CpmApplication implements CommandLineRunner {

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

  @Override
  public void run(String... args) throws Exception {
    var businessGroups = prepareData();
    System.out.println(businessGroups);
  }

  @Transactional
  public Application prepareData() {
    Application app = new Application(0L, "someapp");
    var id = appRepo.save(app);
    return new Application(id, app.code());
    // System.out.println("app");
    // var domain = domainObjectRepo.save(new Domain("someService", app));
    // System.out.println("domain");
    // var br1 = new BusinessRole(null, "br1", domain);
    // var br2 = new BusinessRole(null, "br2", domain);
    // // var businessRoles = businessRoleRepo.saveAll(List.of(br1, br2));
    // System.out.println("br1, br2");
    // var br3 = new BusinessRole(null, "br3", domain);
    // businessRoleRepo.save(br1);
    // businessRoleRepo.save(br2);
    // businessRoleRepo.save(br3);
    //
    // var g1 = new BusinessGroup(null, "CatsReaders", domain, Set.of(br1, br2));
    // // var g2 = new BusinessGroup(null, "CatsWriters", domain, Set.of(br1, br2));
    // // groupRepository.saveAll(List.of(g1, g2));
    // groupRepository.saveAll(List.of(g1));
    // return groupRepository.listByDomain(domain.getCode());
  }
}
