package com.walmart.labs.domain;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Lob;

/** For fields' documentations refer to {@link Task} domain */
@Entity
public class TaskTemplate {
  private String name;

  @Lob private String description;

  @Lob private String note;

  /** Frontend can use duration to calculate the estimated finishing date */
  private long estimatedDuration;

  private boolean isRecurring;

  private String recurringPeriodCronExpression;

  private List<User> assignedStaffList;

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

  public List<User> getAssignedStaffList() {
    return assignedStaffList;
  }

  public void setAssignedStaffList(List<User> assignedStaffList) {
    this.assignedStaffList = assignedStaffList;
  }

  public List<User> getManagerList() {
    return managerList;
  }

  public void setManagerList(List<User> managerList) {
    this.managerList = managerList;
  }
}
