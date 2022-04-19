package dev.rmaiun.cpm.repository;

import java.util.HashMap;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

public abstract class GenericRepository<T extends DbMapper> {

  private final NamedParameterJdbcTemplate jdbcTemplate;

  public GenericRepository(NamedParameterJdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  protected abstract String table();

  public long save(T entity) {
    if (jdbcTemplate.getJdbcTemplate().getDataSource() == null) {
      throw new RuntimeException("Data Source is not defined");
    }
    var simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate().getDataSource())
        .withTableName(table()).usingGeneratedKeyColumns("id");
    return simpleJdbcInsert.executeAndReturnKey(entity.asSource()).longValue();
  }

  public long delete(Long id) {
    var query = String.format("DELETE FROM %s WHERE id = :id", table());
    var params = new MapSqlParameterSource("id", id);
    return jdbcTemplate.update(query, params);
  }

  public long clearTable() {
    var query = String.format("DELETE FROM %s", table());
    return jdbcTemplate.update(query, new HashMap<>());
  }

}
