package dev.rmaiun.cpm.repository;

import dev.rmaiun.cpm.doman.BusinessRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessRoleRepository extends JpaRepository<BusinessRole, Long> {

}
