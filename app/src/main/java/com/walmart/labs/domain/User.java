package com.walmart.labs.domain;

import java.util.Collection;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
public class User extends BasicDomain implements UserDetails {
  /** List of allowedRoleSet the use has */
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
    name = "user_role_mapping",
    joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
  )
  private Set<UserRole> allowedRoleSet;

  @ManyToMany(mappedBy = "staffSet", fetch = FetchType.EAGER)
  private Set<Task> staffTaskSet;

  @ManyToMany(mappedBy = "managerSet", fetch = FetchType.EAGER)
  private Set<Task> managerTaskSet;

  @ManyToMany(mappedBy = "staffSet", fetch = FetchType.EAGER)
  private Set<TaskTemplate> staffTaskTemplateSet;

  @ManyToMany(mappedBy = "managerSet", fetch = FetchType.EAGER)
  private Set<TaskTemplate> managerTaskTemplateSet;

  @ManyToOne private Corporation corporation;

  private String username;

  private String password;

  private String fullName;

  private String emailAddress;

  // TODO: consider enabled and deleted properties in service logic
  private boolean enabled;

  private boolean deleted;

  private boolean accountNonExpired;

  private boolean accountNonLocked;

  private boolean credentialsNonExpired;

  public Set<UserRole> getAllowedRoleSet() {
    return allowedRoleSet;
  }

  public void setAllowedRoleSet(Set<UserRole> allowedRoleSet) {
    this.allowedRoleSet = allowedRoleSet;
  }

  public Corporation getCorporation() {
    return corporation;
  }

  public void setCorporation(Corporation corporation) {
    this.corporation = corporation;
  }

  public Set<Task> getStaffTaskSet() {
    return staffTaskSet;
  }

  public void setStaffTaskSet(Set<Task> staffTaskSet) {
    this.staffTaskSet = staffTaskSet;
  }

  public Set<Task> getManagerTaskSet() {
    return managerTaskSet;
  }

  public void setManagerTaskSet(Set<Task> managerTaskSet) {
    this.managerTaskSet = managerTaskSet;
  }

  public Set<TaskTemplate> getStaffTaskTemplateSet() {
    return staffTaskTemplateSet;
  }

  public void setStaffTaskTemplateSet(Set<TaskTemplate> staffTaskTemplateSet) {
    this.staffTaskTemplateSet = staffTaskTemplateSet;
  }

  public Set<TaskTemplate> getManagerTaskTemplateSet() {
    return managerTaskTemplateSet;
  }

  public void setManagerTaskTemplateSet(Set<TaskTemplate> managerTaskTemplateSet) {
    this.managerTaskTemplateSet = managerTaskTemplateSet;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return allowedRoleSet;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
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

  @Override
  public boolean isAccountNonExpired() {
    return accountNonExpired;
  }

  public void setAccountNonExpired(boolean accountNonExpired) {
    this.accountNonExpired = accountNonExpired;
  }

  @Override
  public boolean isAccountNonLocked() {
    return accountNonLocked;
  }

  public void setAccountNonLocked(boolean accountNonLocked) {
    this.accountNonLocked = accountNonLocked;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return credentialsNonExpired;
  }

  public void setCredentialsNonExpired(boolean credentialsNonExpired) {
    this.credentialsNonExpired = credentialsNonExpired;
  }
}
