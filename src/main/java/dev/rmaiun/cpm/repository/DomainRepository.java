package dev.rmaiun.cpm.repository;

import dev.rmaiun.cpm.doman.Domain;
import dev.rmaiun.cpm.repository.core.GenericRepository;
import java.util.Optional;
import javax.swing.text.html.Option;
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

  public Optional<Domain> findDomainByApplication(String domainCode, String appCode) {
    return null;
  }

  @Override
  public SqlParameterSource parameterSource(Domain o) {
    return new MapSqlParameterSource("id", o.id())
        .addValue("code", o.code())
        .addValue("app_id", o.appId());
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
}
