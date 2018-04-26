package com.walmart.labs.domain;

import java.util.Collection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

@Entity
public class User extends BasicDomain implements UserDetails {
  /** List of allowedRoleList the use has */
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "user_role_mapping",
      joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
  )
  private Collection<UserRole> allowedRoleList;

  @ManyToMany(mappedBy = "staffList")
  private Collection<Task> staffTasks;

  @ManyToMany(mappedBy = "managerList")
  private Collection<Task> managerTasks;

  @ManyToMany(mappedBy = "staffList")
  private Collection<TaskTemplate> staffTaskTemplates;

  @ManyToMany(mappedBy = "managerList")
  private Collection<TaskTemplate> managerTaskTemplates;

  @OneToOne private Corporation corporation;

  private String username;

  private String password;

  private boolean enabled;

  private boolean deleted;

  private boolean accountNonExpired;

  private boolean accountNonLocked;

  private boolean credentialsNonExpired;

  public Collection<UserRole> getAllowedRoleList() {
    return allowedRoleList;
  }

  public void setAllowedRoleList(Collection<UserRole> allowedRoleList) {
    this.allowedRoleList = allowedRoleList;
  }

  public Corporation getCorporation() {
    return corporation;
  }

  public void setCorporation(Corporation corporation) {
    this.corporation = corporation;
  }

  public Collection<Task> getStaffTasks() {
    return staffTasks;
  }

  public void setStaffTasks(Collection<Task> staffTasks) {
    this.staffTasks = staffTasks;
  }

  public Collection<Task> getManagerTasks() {
    return managerTasks;
  }

  public void setManagerTasks(Collection<Task> managerTasks) {
    this.managerTasks = managerTasks;
  }

  public Collection<TaskTemplate> getStaffTaskTemplates() {
    return staffTaskTemplates;
  }

  public void setStaffTaskTemplates(Collection<TaskTemplate> staffTaskTemplates) {
    this.staffTaskTemplates = staffTaskTemplates;
  }

  public Collection<TaskTemplate> getManagerTaskTemplates() {
    return managerTaskTemplates;
  }

  public void setManagerTaskTemplates(Collection<TaskTemplate> managerTaskTemplates) {
    this.managerTaskTemplates = managerTaskTemplates;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return allowedRoleList;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
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
