package dev.rmaiun.cpm.repository;

import dev.rmaiun.cpm.doman.Domain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DomainRepository extends JpaRepository<Domain, Long> {

}
