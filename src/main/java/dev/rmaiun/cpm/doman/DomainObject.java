package dev.rmaiun.cpm.doman;

import java.util.StringJoiner;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "object")
public class DomainObject {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column
  private String code;
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
  @JoinColumn(name = "app_id")
  private Application app;

  public DomainObject() {
  }

  public DomainObject(String code, Application app) {
    this.id = id;
    this.code = code;
    this.app = app;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Application getApp() {
    return app;
  }

  public void setApp(Application app) {
    this.app = app;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", DomainObject.class.getSimpleName() + "[", "]")
        .add("id=" + id)
        .add("code='" + code + "'")
        .add("app=" + app)
        .toString();
  }
}
