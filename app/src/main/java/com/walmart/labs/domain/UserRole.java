package com.walmart.labs.domain;

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "role")
public class UserRole extends BasicDomain implements GrantedAuthority {
  /**
   * The name of the roles and privilege you create in your database would be the GrantedAuthority
   * representation, e.g. "ROLE_ADMIN", "PRIVILEGE_WRITE" etc. When a user is authenticated, make
   * sure that all GrantedAuthorities of all its roles and the corresponding allowedPrivilegeSet are
   * returned from the UserDetails.getAuthorities() method.
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
   * <p>The ApplicationUserDetailsService would take care to collect all roles and all operations of
   * those roles and make them available by the method getAuthorities() in the returned UserDetails
   * instance.
   *
   * <p>Same as {@link RolePrivilege}
   */
  private String name;

  /**
   * MappedBy list of user because of the ManyToMany relationship between {@link User} and {@link
   * UserRole}
   */
  @ManyToMany(mappedBy = "allowedRoleSet")
  private Set<User> userSet;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
    name = "role_privilege_mapping",
    joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "privilege_id", referencedColumnName = "id")
  )
  private Set<RolePrivilege> allowedPrivilegeSet;

  public UserRole() {}

  public UserRole(String name) {
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

  public Set<User> getUserSet() {
    return userSet;
  }

  public void setUserSet(Set<User> userSet) {
    this.userSet = userSet;
  }

  public Set<RolePrivilege> getAllowedPrivilegeSet() {
    return allowedPrivilegeSet;
  }

  public void setAllowedPrivilegeSet(Set<RolePrivilege> allowedPrivilegeSet) {
    this.allowedPrivilegeSet = allowedPrivilegeSet;
  }
}
