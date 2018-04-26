package com.walmart.labs.domain;

import java.util.Collection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

@Entity
public class User extends BasicDomain {
  /** List of allowedRoleList the use has */
  @ManyToMany
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
}
