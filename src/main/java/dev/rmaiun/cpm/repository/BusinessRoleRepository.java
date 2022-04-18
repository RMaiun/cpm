package dev.rmaiun.cpm.repository;

import dev.rmaiun.cpm.doman.BusinessRole;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessRoleRepository extends JpaRepository<BusinessRole, Long> {

  @Query("select br from BusinessRole br  where br.id = :parentId")
  List<BusinessRole> findByParent(@Param("parentId")Long id);
}
