package dev.rmaiun.cpm.doman;

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
@Table(name = "business_role")
public class BusinessRole {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column
  private String code;
  @Column
  private String workspace;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
  @JoinColumn(name = "object_id", nullable = false)
  private DomainObject object;

  public BusinessRole() {
  }

  public BusinessRole(Long id, String code, String workspace, DomainObject object) {
    this.id = id;
    this.code = code;
    this.workspace = workspace;
    this.object = object;
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

  public String getWorkspace() {
    return workspace;
  }

  public void setWorkspace(String workspace) {
    this.workspace = workspace;
  }

  public DomainObject getObject() {
    return object;
  }

  public void setObject(DomainObject object) {
    this.object = object;
  }
}
