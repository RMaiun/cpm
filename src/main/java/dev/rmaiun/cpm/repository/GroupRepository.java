package dev.rmaiun.cpm.repository;

import dev.rmaiun.cpm.doman.BusinessGroup;
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
public class GroupRepository extends GenericRepository<BusinessGroup> {

  public GroupRepository(NamedParameterJdbcTemplate jdbcTemplate) {
    super(jdbcTemplate);
  }

  public List<BusinessGroup> listByApp(String app) {
    var query = """
        select * from business_group
        inner join application a on a.id = business_group.app_id
        where a.code = :appCode
        """;
    var params = new MapSqlParameterSource("appCode", app);
    return jdbcTemplate.query(query, params, rowMapper());
  }

  public Optional<BusinessGroup> findByAppCode(String group, String app) {
    var query = """
        select * from business_group bg
        inner join application a on a.id = bg.app_id
        where bg.code = :groupCode and a.code = :appCode
        """;
    var params = new MapSqlParameterSource("groupCode", group)
        .addValue("appCode", app);
    var res = jdbcTemplate.query(query, params, rowMapper());
    return Optional.ofNullable(DataAccessUtils.singleResult(res));
  }

  public List<BusinessGroup> listAssignedGroupsForUser(String user, String app, String domain) {
    var query = """
        select bg.id, bg.code, bg.app_id from user_group ug
        inner join business_group bg on bg.id = ug.group_id
        inner join application a on a.id = bg.app_id
        inner join group_role gr on bg.id = gr.group_id
        inner join business_role br on br.id = gr.br_id
        inner join domain d on d.id = br.domain_id
        where ug.uid = :uid and a.code = :appCode and d.code = :domainCode
        """;
    var params = new MapSqlParameterSource("uid", user)
        .addValue("appCode", app)
        .addValue("domainCode", domain);
    return jdbcTemplate.query(query, params, rowMapper());
  }

  public List<BusinessGroup> listGroupAssignedToDomainWriters(String app, String domain) {
    var query = """
        select bg.id, bg.code, bg.app_id from group_role gr
        inner join business_group bg on bg.id = gr.group_id
        inner join business_role br on gr.br_id = br.id
        inner join domain d on br.domain_id = d.id
        inner join application a on a.id = bg.app_id
        where a.code = :appCode and d.code = :domainCode
        """;

    var params = new MapSqlParameterSource("appCode", app)
        .addValue("domainCode", domain);
    return jdbcTemplate.query(query, params, rowMapper());
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
}
