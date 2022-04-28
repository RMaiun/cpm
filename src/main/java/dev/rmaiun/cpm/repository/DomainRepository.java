package dev.rmaiun.cpm.repository;

import dev.rmaiun.cpm.doman.Domain;
import dev.rmaiun.cpm.repository.core.GenericRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.support.DataAccessUtils;
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
    var query = """
        select * from domain d
        inner join application a on a.id = d.app_id
        where a.code = :appCode and d.code = :domainCode
        """;
    var params = new MapSqlParameterSource("appCode", appCode)
        .addValue("domainCode", domainCode);
    var res = jdbcTemplate.query(query, params, rowMapper());
    return Optional.ofNullable(DataAccessUtils.singleResult(res));
  }


  public List<Domain> listDomainByApplication(String appCode) {
    var query = """
        select * from domain d
        inner join application a on a.id = d.app_id
        where a.code = :appCode
        """;
    var params = new MapSqlParameterSource("appCode", appCode);
    return jdbcTemplate.query(query, params, rowMapper());
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
