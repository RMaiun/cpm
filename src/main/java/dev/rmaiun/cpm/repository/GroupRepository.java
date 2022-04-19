package dev.rmaiun.cpm.repository;

import dev.rmaiun.cpm.doman.BusinessGroup;
import dev.rmaiun.cpm.doman.view.GroupView;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GroupRepository extends JpaRepository<BusinessGroup, Long> {

  @Query("select g from BusinessGroup g join fetch g.domain d where d.code = ?1")
  List<GroupView> listByDomain(String domain);
}
