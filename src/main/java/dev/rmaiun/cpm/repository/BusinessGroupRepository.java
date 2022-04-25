package dev.rmaiun.cpm.repository;

import dev.rmaiun.cpm.doman.Application;
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
  public SqlParameterSource parameterSource(BusinessGroup o) {
    return new MapSqlParameterSource("id", o.id())
        .addValue("code", o.code())
        .addValue("app_id", o.appId());
  }

  @Override
  public RowMapper<BusinessGroup> rowMapper() {
    return (rs, rowNum) -> {
      var id = rs.getLong("id");
      var code = rs.getString("code");
      var appId = rs.getLong("app_id");
      return new BusinessGroup(id, code, appId);
    };
  }

  @Override
  protected String table() {
    return "business_group";
  }
  @Override
  public Class<BusinessGroup> clazz() {
    return BusinessGroup.class;
  }
}
