package dev.rmaiun.cpm.repository;

import dev.rmaiun.cpm.doman.Application;
import dev.rmaiun.cpm.repository.core.GenericRepository;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

@Component
public class ApplicationRepository extends GenericRepository<Application> {

  public ApplicationRepository(NamedParameterJdbcTemplate jdbcTemplate) {
    super(jdbcTemplate);
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
