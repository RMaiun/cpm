package dev.rmaiun.cpm.repository;

import dev.rmaiun.cpm.doman.UserGroupRelation;
import dev.rmaiun.cpm.repository.core.GenericRepository;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

@Component
public class UserGroupRelationRepository extends GenericRepository<UserGroupRelation> {

  public UserGroupRelationRepository(NamedParameterJdbcTemplate jdbcTemplate) {
    super(jdbcTemplate);
  }

  @Override
  public SqlParameterSource parameterSource(UserGroupRelation o) {
    return new MapSqlParameterSource("uid", o.uid())
        .addValue("group_id", o.groupId());
  }

  @Override
  public RowMapper<UserGroupRelation> rowMapper() {
    return (rs, rowNum) -> {
      var uid = rs.getString("uid");
      var gid = rs.getLong("group_id");
      return new UserGroupRelation(uid, gid);
    };
  }

  @Override
  protected String table() {
    return "user_group";
  }

  @Override
  public Class<UserGroupRelation> clazz() {
    return UserGroupRelation.class;
  }
}
