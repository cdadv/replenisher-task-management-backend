package com.walmart.labs.domain;

import java.util.Collection;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "privilege")
public class RolePrivilege extends BasicDomain implements GrantedAuthority {
  /**
   * The name of the roleList and privilege you create in your database would be the
   * GrantedAuthority representation, e.g. "ROLE_ADMIN", "PRIVILEGE_WRITE" etc. When a user is
   * authenticated, make sure that all GrantedAuthorities of all its roleList and the corresponding
   * privileges are returned from the UserDetails.getAuthorities() method.
   *
   * <p>Example: The admin role with id ROLE_ADMIN has the operations PRIVILEGE_WRITE,
   * PRIVILEGE_READ assigned to it. The user role with id ROLE_USER has the operation
   * PRIVILEGE_READ.
   *
   * <p>If an admin logs in the resulting security context will have the GrantedAuthorities:
   * ROLE_ADMIN, PRIVILEGE_WRITE, PRIVILEGE_READ
   *
   * <p>If a user logs it, it will have: ROLE_USER, PRIVILEGE_READ
   *
   * <p>The UserDetailsService would take care to collect all roleList and all operations of those
   * roleList and make them available by the method getAuthorities() in the returned UserDetails
   * instance.
   *
   * <p>Same as {@link UserRole}
   */
  private String name;

  @ManyToMany(mappedBy = "allowedPrivilegeList")
  private Collection<UserRole> roleList;

  public RolePrivilege() {}

  public RolePrivilege(String name) {
    this.name = name;
  }

  @Override
  public String getAuthority() {
    return name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Collection<UserRole> getRoleList() {
    return roleList;
  }

  public void setRoleList(Collection<UserRole> roleList) {
    this.roleList = roleList;
  }
}
