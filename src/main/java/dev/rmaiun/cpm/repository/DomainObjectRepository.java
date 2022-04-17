package dev.rmaiun.cpm.repository;

import dev.rmaiun.cpm.doman.DomainObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DomainObjectRepository extends JpaRepository<DomainObject, Long> {

}
