package dev.rmaiun.cpm.repository;

import dev.rmaiun.cpm.doman.BusinessRole;
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
  public SqlParameterSource parameterSource(BusinessRole object) {
    return new MapSqlParameterSource("id", object.id())
        .addValue("code", object.code())
        .addValue("domain_id", object.domainId());
  }

  @Override
  public RowMapper<BusinessRole> rowMapper() {
    return (rs, rowNum) -> {
      var id = rs.getLong(0);
      var code = rs.getString(1);
      var domId = rs.getLong(2);
      return new BusinessRole(id, code, domId);
    };
  }

  @Override
  protected String table() {
    return "business_role";
  }
}
