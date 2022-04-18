// package dev.rmaiun.cpm.doman;
//
// import java.io.Serializable;
// import javax.persistence.Embeddable;
// import org.apache.commons.lang3.builder.EqualsBuilder;
// import org.apache.commons.lang3.builder.HashCodeBuilder;
//
// @Embeddable
// public class PrimaryKey implements Serializable {
//
//   private Long roleId;
//   private Long parentId;
//
//   public Long getRoleId() {
//     return roleId;
//   }
//
//   public void setRoleId(Long roleId) {
//     this.roleId = roleId;
//   }
//
//   public Long getParentId() {
//     return parentId;
//   }
//
//   public void setParentId(Long parentId) {
//     this.parentId = parentId;
//   }
//
//   @Override
//   public boolean equals(Object o) {
//     if (this == o) {
//       return true;
//     }
//
//     if (!(o instanceof PrimaryKey)) {
//       return false;
//     }
//
//     PrimaryKey that = (PrimaryKey) o;
//
//     return new EqualsBuilder().append(roleId, that.roleId).append(parentId, that.parentId).isEquals();
//   }
//
//   @Override
//   public int hashCode() {
//     return new HashCodeBuilder(17, 37).append(roleId).append(parentId).toHashCode();
//   }
// }
