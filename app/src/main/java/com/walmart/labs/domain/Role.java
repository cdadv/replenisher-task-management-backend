package com.walmart.labs.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import org.springframework.security.core.GrantedAuthority;

@Entity
public class Role implements GrantedAuthority {

  @OneToMany private final List<Operation> allowedOperationList = new ArrayList<>();
  /**
   * The ids of the roles and operations you create in your database would be the GrantedAuthority
   * representation, e.g. "ROLE_ADMIN", "OP_DELETE_ACCOUNT" etc. When a user is authenticated, make
   * sure that all GrantedAuthorities of all its roles and the corresponding operations are returned
   * from the UserDetails.getAuthorities() method.
   *
   * <p>Example: The admin role with id ROLE_ADMIN has the operations OP_DELETE_ACCOUNT,
   * OP_READ_ACCOUNT, OP_RUN_BATCH_JOB assigned to it. The user role with id ROLE_USER has the
   * operation OP_READ_ACCOUNT.
   *
   * <p>If an admin logs in the resulting security context will have the GrantedAuthorities:
   * ROLE_ADMIN, OP_DELETE_ACCOUNT, OP_READ_ACCOUNT, OP_RUN_BATCH_JOB
   *
   * <p>If a user logs it, it will have: ROLE_USER, OP_READ_ACCOUNT
   *
   * <p>The UserDetailsService would take care to collect all roles and all operations of those
   * roles and make them available by the method getAuthorities() in the returned UserDetails
   * instance.
   *
   * <p>Same as {@link Operation}
   */
  @Id private String id;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public List<Operation> getAllowedOperationList() {
    return allowedOperationList;
  }

  @Override
  public String getAuthority() {
    return id;
  }
}
