package dev.rmaiun.cpm.repository;

import dev.rmaiun.cpm.doman.BusinessRole;
import dev.rmaiun.cpm.doman.RoleType;
import dev.rmaiun.cpm.repository.core.GenericRepository;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

@Component
public class BusinessRoleRepository extends GenericRepository<BusinessRole> {

  public BusinessRoleRepository(NamedParameterJdbcTemplate jdbcTemplate) {
    super(jdbcTemplate);
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
