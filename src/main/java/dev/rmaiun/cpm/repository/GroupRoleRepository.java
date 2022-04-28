package dev.rmaiun.cpm.repository;

import dev.rmaiun.cpm.doman.BusinessGroup;
import dev.rmaiun.cpm.doman.GroupRoleRelation;
import dev.rmaiun.cpm.repository.core.GenericRepository;
import java.util.List;
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

  public void deleteByGroupIds(List<Long> groupIds) {
    var query = "DELETE FROM group_role WHERE group_role.group_id in (:ids)";
    var params = new MapSqlParameterSource("ids", groupIds);
    jdbcTemplate.update(query, params);
  }

  public List<BusinessGroup> listGroupAssignedToDomainWriters(String app, String domain) {
    return null;
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
