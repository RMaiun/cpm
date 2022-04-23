package dev.rmaiun.cpm.repository;

import dev.rmaiun.cpm.doman.Application;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
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

  @Override
  public SqlParameterSource parameterSource(Application object) {
    return new MapSqlParameterSource("id", object.id())
        .addValue("code", object.code());
  }

  @Override
  public RowMapper<Application> rowMapper() {
    return (rs, rowNum) -> new Application(rs.getLong("id"), rs.getString("code"));
  }
}
