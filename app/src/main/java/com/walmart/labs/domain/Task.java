package com.walmart.labs.domain;

import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Lob;

@Entity
public class Task extends BasicDomain {
  /** task name */
  private String name;
  /** {@link TaskStatus} are pre-defined enums for tracking status(progress) of tasks. */
  private TaskStatus taskStatus;
  /**
   * Claim that notes field is created as LongText type column in database for storing descriptions,
   * notes and feedback.
   */
  @Lob private String description;

  @Lob private String note;

  @Lob private String feedback;

  /**
   * Created task time for getting inputted priority. Created time is passed in and generated by
   * frontend.
   */
  private Date timeInput;

  /**
   * Estimated finish task time for getting time estimated to perform the task. Frontend may pass in
   * an estimated finish time in {@link Date} format. Backend will calculate the difference between
   * created time and finish time to get the duration.
   */
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

  /** List of staffs that are assigned to this task */
  private List<User> assignedStaffList;

  /** List of managers that manage this task */
  private List<User> managerList;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public TaskStatus getTaskStatus() {
    return taskStatus;
  }

  public void setTaskStatus(TaskStatus taskStatus) {
    this.taskStatus = taskStatus;
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

  public String getFeedback() {
    return feedback;
  }

  public void setFeedback(String feedback) {
    this.feedback = feedback;
  }

  public Date getTimeInput() {
    return timeInput;
  }

  public void setTimeInput(Date timeInput) {
    this.timeInput = timeInput;
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
