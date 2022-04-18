// package dev.rmaiun.cpm.doman;
//
// import javax.persistence.Entity;
// import javax.persistence.JoinColumn;
// import javax.persistence.ManyToOne;
// import javax.persistence.Table;
//
// @Entity
// @Table(name = "business_role_relation")
// // @IdClass(PrimaryKey.class)
// public class BusinessRoleRelation {
//
//   @ManyToOne
//   @JoinColumn(name = "role_id", nullable = false)
//   private BusinessRole role;
//   @ManyToOne
//   @JoinColumn(name = "parent_id", nullable = false)
//   private BusinessRole parent;
//
//   public BusinessRoleRelation() {
//   }
//
//   public BusinessRoleRelation(BusinessRole role, BusinessRole parent) {
//     this.role = role;
//     this.parent = parent;
//   }
//
//   public BusinessRole getRole() {
//     return role;
//   }
//
//   public void setRole(BusinessRole role) {
//     this.role = role;
//   }
//
//   public BusinessRole getParent() {
//     return parent;
//   }
//
//   public void setParent(BusinessRole parent) {
//     this.parent = parent;
//   }
// }
