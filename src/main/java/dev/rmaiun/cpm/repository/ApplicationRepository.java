package dev.rmaiun.cpm.repository;

import dev.rmaiun.cpm.doman.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

}
