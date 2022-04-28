package dev.rmaiun.cpm.repository;

import dev.rmaiun.cpm.doman.BusinessRole;
import dev.rmaiun.cpm.doman.RoleType;
import dev.rmaiun.cpm.repository.core.GenericRepository;
import java.util.List;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

@Component
public class RoleRepository extends GenericRepository<BusinessRole> {

  public RoleRepository(NamedParameterJdbcTemplate jdbcTemplate) {
    super(jdbcTemplate);
  }

  public List<BusinessRole> findRolesByAppDomain(String app, String domain) {
    var query = """
        select * from business_role br
        inner join domain d on br.domain_id = d.id
        inner join application a on a.id = d.app_id
        where a.code = :appCode and d.code = :domainCode
        """;
    var params = new MapSqlParameterSource("appCode", app)
        .addValue("domainCode", domain);
    return jdbcTemplate.query(query, params, rowMapper());
  }

  public List<BusinessRole> findRolesByApp(Long appId) {
    var query = """
        select * from business_role br
        inner join domain d on br.domain_id = d.id
        where d.app_id = :id
        """;
    var params = new MapSqlParameterSource("id", appId);
    return jdbcTemplate.query(query, params, rowMapper());
  }

  @Override
  public SqlParameterSource parameterSource(BusinessRole o) {
    return new MapSqlParameterSource("id", o.id())
        .addValue("domain_id", o.domainId())
        .addValue("role_type", o.type());
  }

  @Override
  public RowMapper<BusinessRole> rowMapper() {
    return (rs, rowNum) -> {
      var id = rs.getLong("id");
      var domId = rs.getLong("domain_id");
      var type = rs.getString("role_type");
      return new BusinessRole(id, domId, RoleType.valueOf(type));
    };
  }

  @Override
  protected String table() {
    return "business_role";
  }
}
