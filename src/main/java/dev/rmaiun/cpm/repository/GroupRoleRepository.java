package dev.rmaiun.cpm.repository;

import dev.rmaiun.cpm.doman.GroupRoleRelation;
import dev.rmaiun.cpm.repository.core.GenericRepository;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

@Component
public class GroupRoleRepository extends GenericRepository<GroupRoleRelation> {

  public GroupRoleRepository(NamedParameterJdbcTemplate jdbcTemplate) {
    super(jdbcTemplate);
  }

  @Override
  public SqlParameterSource parameterSource(GroupRoleRelation o) {
    return new MapSqlParameterSource("group_id", o.groupId())
        .addValue("br_id", o.roleId());
  }

  @Override
  public RowMapper<GroupRoleRelation> rowMapper() {
    return (rs, rowNum) -> {
      var gid = rs.getLong("group_id");
      var rid = rs.getLong("br_id");
      return new GroupRoleRelation(gid, rid);
    };
  }

  @Override
  protected String table() {
    return "group_role";
  }
}
