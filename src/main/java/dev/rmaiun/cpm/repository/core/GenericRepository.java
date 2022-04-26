package dev.rmaiun.cpm.repository.core;

import dev.rmaiun.cpm.exception.DatabaseException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

public abstract class GenericRepository<T> {

  protected final NamedParameterJdbcTemplate jdbcTemplate;

  public GenericRepository(NamedParameterJdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public long save(T entity) {
    if (jdbcTemplate.getJdbcTemplate().getDataSource() == null) {
      throw new RuntimeException("Data Source is not defined");
    }
    var simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate().getDataSource())
        .withTableName(table()).usingGeneratedKeyColumns("id");
    return simpleJdbcInsert.executeAndReturnKey(parameterSource(entity)).longValue();
  }

  public void saveWithoutId(T entity) {
    if (jdbcTemplate.getJdbcTemplate().getDataSource() == null) {
      throw new DatabaseException("Data Source is not defined");
    }
    var paramSource = parameterSource(entity);
    var simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate().getDataSource())
        .usingColumns(Objects.requireNonNull(paramSource.getParameterNames()))
        .withTableName(table());
    simpleJdbcInsert.execute(paramSource);
  }

  public long batchSave(Collection<T> entity) {
    if (jdbcTemplate.getJdbcTemplate().getDataSource() == null) {
      throw new RuntimeException("Data Source is not defined");
    }
    var simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate().getDataSource())
        .withTableName(table()).usingGeneratedKeyColumns("id");
    var sourceList = entity.stream().map(this::parameterSource).toArray(SqlParameterSource[]::new);
    return Arrays.stream(simpleJdbcInsert.executeBatch(sourceList)).reduce(0, Integer::sum);
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

  protected abstract SqlParameterSource parameterSource(T o);

  protected abstract RowMapper<T> rowMapper();

  protected abstract String table();

}
