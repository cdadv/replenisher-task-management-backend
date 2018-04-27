package com.walmart.labs.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.walmart.labs.domain.Task;
import java.util.Date;
import java.util.Set;

/**
 * TaskDTO used for creating and editing task object
 *
 * <p>For more fields information refer to {@link Task}
 */
public class TaskDTO {
  // required field for editing
  // Ignoring property when deserializing
  @JsonIgnore
  private Long id;
  // required field for creating
  private String name;
  // required field for creating
  private String taskStatusString;
  // optional field for creating
  private String taskPriorityString;
  // optional field for creating
  private String description;
  // optional field for creating
  private String note;
  // optional field for creating
  private String feedback;
  // required field for creating
  // new Date(timeInputLong * 1000);
  private Date timeInput;
  // required field for creating
  private Date timeEstimatedFinish;
  // optional field for creating
  private boolean isRecurring;
  // optional field for creating
  private String recurringPeriodCronExpression;
  // required field for creating
  private Long corporationId;
  // required field for creating
  private Set<Long> assignedStaffIdSet;
  // required field for creating
  private Set<Long> managerIdSet;

  @JsonProperty
  public Long getId() {
    return id;
  }

  @JsonIgnore
  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getTaskStatusString() {
    return taskStatusString;
  }

  public void setTaskStatusString(String taskStatusString) {
    this.taskStatusString = taskStatusString;
  }

  public String getTaskPriorityString() {
    return taskPriorityString;
  }

  public void setTaskPriorityString(String taskPriorityString) {
    this.taskPriorityString = taskPriorityString;
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

  public Date getTimeEstimatedFinish() {
    return timeEstimatedFinish;
  }

  public void setTimeEstimatedFinish(Date timeEstimatedFinish) {
    this.timeEstimatedFinish = timeEstimatedFinish;
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

  public Long getCorporationId() {
    return corporationId;
  }

  public void setCorporationId(Long corporationId) {
    this.corporationId = corporationId;
  }

  public Set<Long> getAssignedStaffIdSet() {
    return assignedStaffIdSet;
  }

  public void setAssignedStaffIdSet(Set<Long> assignedStaffIdSet) {
    this.assignedStaffIdSet = assignedStaffIdSet;
  }

  public Set<Long> getManagerIdSet() {
    return managerIdSet;
  }

  public void setManagerIdSet(Set<Long> managerIdSet) {
    this.managerIdSet = managerIdSet;
  }
}
