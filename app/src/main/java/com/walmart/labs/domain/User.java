package com.walmart.labs.domain;

import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class User extends BasicDomain {
  /** List of roles the use has */
  private final Collection<UserRole> userRoleList = new ArrayList<>();
  @OneToOne private Corporation corporation;
  private String username;

  private String password;

  private boolean enabled;

  private boolean deleted;

  public Corporation getCorporation() {
    return corporation;
  }

  public void setCorporation(Corporation corporation) {
    this.corporation = corporation;
  }

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

  public Collection<UserRole> getUserRoleList() {
    return userRoleList;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }
}
