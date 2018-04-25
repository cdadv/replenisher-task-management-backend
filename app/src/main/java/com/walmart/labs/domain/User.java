package com.walmart.labs.domain;

import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.Entity;

@Entity
public class User extends BasicDomain {
  /** List of roles the use has */
  private final Collection<Role> roleList = new ArrayList<>();

  private String username;
  private String password;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Collection<Role> getRoleList() {
    return roleList;
  }
}
