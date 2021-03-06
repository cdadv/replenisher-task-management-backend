package com.walmart.labs.domain;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import org.hibernate.annotations.Type;

/**
 * For fields' documentations refer to {@link Task} domain.
 *
 * <p>Difference between Task and TaskTemplate: TaskTemplate does not have TaskStatus, feedback,
 * timeInput, timeEstimatedFinish (estimatedDuration instead),
 */
@Entity
public class TaskTemplate extends BasicDomain {
  private String name;

  @Column(length = 65535, columnDefinition = "Text")
  @Type(type = "text")
  private String description;

  @Column(length = 65535, columnDefinition = "Text")
  @Type(type = "text")
  private String note;

  @Enumerated(EnumType.STRING)
  private TaskPriority taskPriority;

  /** Frontend can use duration to calculate the estimated finishing date */
  private long estimatedDuration;

  /** boolean value to represent if the task is a recurring task */
  private boolean isRecurring;

  /**
   * if the boolean value above is true. This field is for defining the recurring periods with cron
   * job expression.
   *
   * <p>TODO: may change to use enum class with WEEKLY, MONTHLY, etc expressions.
   */
  private String recurringPeriodCronExpression;

  @ManyToOne private Corporation corporation;

  @ManyToMany
  @JoinTable(
    name = "task_template_staff_user_mapping",
    joinColumns = @JoinColumn(name = "task_template_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "staff_user_id", referencedColumnName = "id")
  )
  private Set<User> staffSet;

  @ManyToMany
  @JoinTable(
    name = "task_template_manager_user_mapping",
    joinColumns = @JoinColumn(name = "task_template_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "manager_user_id", referencedColumnName = "id")
  )
  private Set<User> managerSet;

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

  public TaskPriority getTaskPriority() {
    return taskPriority;
  }

  public void setTaskPriority(TaskPriority taskPriority) {
    this.taskPriority = taskPriority;
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

  public Set<User> getStaffSet() {
    return staffSet;
  }

  public void setStaffSet(Set<User> staffSet) {
    this.staffSet = staffSet;
  }

  public Set<User> getManagerSet() {
    return managerSet;
  }

  public void setManagerSet(Set<User> managerSet) {
    this.managerSet = managerSet;
  }
}
