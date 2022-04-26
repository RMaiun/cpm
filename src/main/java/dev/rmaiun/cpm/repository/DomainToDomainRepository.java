package dev.rmaiun.cpm.repository;

import dev.rmaiun.cpm.doman.DomainToDomain;
import dev.rmaiun.cpm.repository.core.GenericRepository;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

@Component
public class DomainToDomainRepository extends GenericRepository<DomainToDomain> {

  public DomainToDomainRepository(NamedParameterJdbcTemplate jdbcTemplate) {
    super(jdbcTemplate);
  }

  @Override
  public SqlParameterSource parameterSource(DomainToDomain o) {
    return new MapSqlParameterSource("id", o.id())
        .addValue("domain_id", o.domainId())
        .addValue("parent_id", o.parentId());
  }

  @Override
  public RowMapper<DomainToDomain> rowMapper() {
    return (rs, rowNum) -> {
      var id = rs.getLong("id");
      var groupId = rs.getLong("domain_id");
      var parentId = rs.getLong("parent_id");
      return new DomainToDomain(id, groupId, parentId);
    };
  }

  @Override
  protected String table() {
    return "domain_to_domain";
  }
}
