package dev.rmaiun.cpm.repository;

import dev.rmaiun.cpm.doman.Application;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class ApplicationRepository extends GenericRepository<Application> {

  private final NamedParameterJdbcTemplate jdbcTemplate;

  public ApplicationRepository(NamedParameterJdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate jdbcTemplate1) {
    super(jdbcTemplate);
    this.jdbcTemplate = jdbcTemplate1;
  }

  @Override
  protected String table() {
    return "application";
  }
}
