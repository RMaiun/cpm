package dev.rmaiun.cpm.repository.core;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

public abstract class GenericRepository<T> implements CommonRepoSupport<T> {

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
    return simpleJdbcInsert.executeAndReturnKey(parameterSource(entity)).longValue();
  }

  public Optional<T> getById(Long id) {
    var query = String.format("SELECT * from %s t where t.id = :id", table());
    var params = new MapSqlParameterSource("id", id);
    List<T> res = jdbcTemplate.query(query, params, rowMapper());
    return Optional.ofNullable(DataAccessUtils.singleResult(res));
  }

  public List<T> listAll() {
    var query = String.format("SELECT * from %s t", table());
    var params = new MapSqlParameterSource();
    return jdbcTemplate.query(query, params, rowMapper());
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

  // public long countRowsBy(){
  //   jdbcTemplate.query("select count()")
  // }

}
