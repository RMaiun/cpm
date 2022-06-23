package dev.rmaiun.cpm.repository;

import dev.rmaiun.cpm.doman.UserGroupRelation;
import dev.rmaiun.cpm.repository.core.GenericRepository;
import java.util.List;
import java.util.Optional;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.dao.support.DataAccessUtils;
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

  public void deleteByGroupIds(List<Long> groupIds) {
    if (CollectionUtils.isNotEmpty(groupIds)) {
      var query = "DELETE FROM user_group WHERE user_group.group_id in (:ids)";
      var params = new MapSqlParameterSource("ids", groupIds);
      jdbcTemplate.update(query, params);
    }
  }

  public boolean relationExists(UserGroupRelation ugr) {
    var query = "SELECT * from user_group ug where ug.uid = :user and ug.group_id = :groupId";
    var params = new MapSqlParameterSource("user", ugr.uid()).addValue("groupId", ugr.groupId());
    List<UserGroupRelation> res = jdbcTemplate.query(query, params, rowMapper());
    return Optional.ofNullable(DataAccessUtils.singleResult(res)).isPresent();
  }

  @Override
  public SqlParameterSource parameterSource(UserGroupRelation o) {
    return new MapSqlParameterSource("uid", o.uid()).addValue("group_id", o.groupId());
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
}
