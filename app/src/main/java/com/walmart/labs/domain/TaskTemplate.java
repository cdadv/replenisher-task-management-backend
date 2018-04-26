package com.walmart.labs.domain;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import org.hibernate.annotations.Type;

/** For fields' documentations refer to {@link Task} domain */
@Entity
public class TaskTemplate extends BasicDomain {
  private String name;

  @Column(length = 65535, columnDefinition = "Text")
  @Type(type = "text")
  private String description;

  @Column(length = 65535, columnDefinition = "Text")
  @Type(type = "text")
  private String note;

  /** Frontend can use duration to calculate the estimated finishing date */
  private long estimatedDuration;

  private boolean isRecurring;

  private String recurringPeriodCronExpression;

  @OneToOne
  private Corporation corporation;

  @ManyToMany
  @JoinTable(
    name = "task_template_staff_user_mapping",
    joinColumns = @JoinColumn(name = "task_template_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "staff_user_id", referencedColumnName = "id")
  )
  private List<User> staffList;

  @ManyToMany
  @JoinTable(
    name = "task_template_manager_user_mapping",
    joinColumns = @JoinColumn(name = "task_template_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "manager_user_id", referencedColumnName = "id")
  )
  private List<User> managerList;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public long getEstimatedDuration() {
    return estimatedDuration;
  }

  public void setEstimatedDuration(long estimatedDuration) {
    this.estimatedDuration = estimatedDuration;
  }

  public boolean isRecurring() {
    return isRecurring;
  }

  public void setRecurring(boolean recurring) {
    isRecurring = recurring;
  }

  public String getRecurringPeriodCronExpression() {
    return recurringPeriodCronExpression;
  }

  public void setRecurringPeriodCronExpression(String recurringPeriodCronExpression) {
    this.recurringPeriodCronExpression = recurringPeriodCronExpression;
  }

  public Corporation getCorporation() {
    return corporation;
  }

  public void setCorporation(Corporation corporation) {
    this.corporation = corporation;
  }

  public List<User> getStaffList() {
    return staffList;
  }

  public void setStaffList(List<User> staffList) {
    this.staffList = staffList;
  }

  public List<User> getManagerList() {
    return managerList;
  }

  public void setManagerList(List<User> managerList) {
    this.managerList = managerList;
  }
}
