package dev.rmaiun.cpm.repository;

import dev.rmaiun.cpm.doman.BusinessGroup;
import dev.rmaiun.cpm.repository.core.GenericRepository;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

@Component
public class BusinessGroupRepository extends GenericRepository<BusinessGroup> {

  public BusinessGroupRepository(NamedParameterJdbcTemplate jdbcTemplate) {
    super(jdbcTemplate);
  }

  @Override
  public SqlParameterSource parameterSource(BusinessGroup object) {
    return new MapSqlParameterSource("id", object.id())
        .addValue("code", object.code())
        .addValue("app_id", object.appId());
  }

  @Override
  public RowMapper<BusinessGroup> rowMapper() {
    return (rs, rowNum) -> {
      var id = rs.getLong(0);
      var code = rs.getString(1);
      var appId = rs.getLong(2);
      return new BusinessGroup(id, code, appId);
    };
  }

  @Override
  protected String table() {
    return "business_group";
  }
}
