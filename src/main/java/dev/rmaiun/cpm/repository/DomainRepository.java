package dev.rmaiun.cpm.repository;

import dev.rmaiun.cpm.doman.Domain;
import dev.rmaiun.cpm.repository.core.GenericRepository;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

@Component
public class DomainRepository extends GenericRepository<Domain> {

  public DomainRepository(NamedParameterJdbcTemplate jdbcTemplate) {
    super(jdbcTemplate);
  }

  @Override
  public SqlParameterSource parameterSource(Domain o) {
    return new MapSqlParameterSource("id", o.getId())
        .addValue("code", o.getCode())
        .addValue("app_id", o.getAppId());
  }

  @Override
  public RowMapper<Domain> rowMapper() {
    return (rs, rowNum) -> {
      var id = rs.getLong("id");
      var code = rs.getString("code");
      var appId = rs.getLong("app_id");
      return new Domain(id, code, appId);
    };
  }

  @Override
  protected String table() {
    return "domain";
  }

  @Override
  public Class<Domain> clazz() {
    return Domain.class;
  }
}
