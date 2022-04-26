package dev.rmaiun.cpm.repository;

import static java.util.Optional.ofNullable;
import static org.springframework.dao.support.DataAccessUtils.singleResult;

import dev.rmaiun.cpm.doman.Application;
import dev.rmaiun.cpm.repository.core.GenericRepository;
import java.util.Optional;
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

  public Optional<Application> getByCode(String code) {
    var query = "SELECT * FROM application WHERE code = :code";
    var params = new MapSqlParameterSource("code", code);
    return ofNullable(singleResult(jdbcTemplate.query(query, params, rowMapper())));
  }


  @Override
  protected String table() {
    return "application";
  }

  @Override
  public SqlParameterSource parameterSource(Application o) {
    return new MapSqlParameterSource("id", o.id())
        .addValue("code", o.code());
  }

  @Override
  public RowMapper<Application> rowMapper() {
    return (rs, rowNum) -> new Application(rs.getLong("id"), rs.getString("code"));
  }
}
