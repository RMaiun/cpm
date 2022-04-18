package dev.rmaiun.cpm.doman;

import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name = "BusinessRole")
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

  @ManyToMany(cascade = {CascadeType.ALL},fetch = FetchType.LAZY)
  @JoinTable(
      name = "business_role_relation",
      joinColumns = {@JoinColumn(name = "parent_id")},
      inverseJoinColumns = {@JoinColumn(name = "role_id")}
  )
  private Set<BusinessRole> children = new HashSet<>();

  public BusinessRole() {
  }

  public BusinessRole(String code, String workspace, DomainObject object) {
    this.code = code;
    this.workspace = workspace;
    this.object = object;
  }

  public Set<BusinessRole> getChildren() {
    return children;
  }

  public void setChildren(Set<BusinessRole> children) {
    this.children = children;
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

  // public BusinessRole getParent() {
  //   return parent;
  // }
  //
  // public void setParent(BusinessRole parent) {
  //   this.parent = parent;
  // }

  @Override
  public String toString() {
    return new StringJoiner(", ", BusinessRole.class.getSimpleName() + "[", "]")
        .add("id=" + id)
        .add("code='" + code + "'")
        .add("workspace='" + workspace + "'")
        .add("object=" + object)
        .add("children=" + children)
        // .add("parent=" + parent)
        .toString();
  }
}
