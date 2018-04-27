package com.walmart.labs.dto;

import com.walmart.labs.domain.Task;
import java.util.Date;
import java.util.List;

/**
 * TaskDTO used for creating and editing task object
 *
 * <p>For more fields information refer to {@link Task}
 */
public class TaskDTO {
  // required field
  private String name;
  // required field
  private String taskStatusString;
  // optional field
  private String description;
  // optional field
  private String note;
  // optional field
  private String feedback;
  // required field
  // new Date(timeInputLong * 1000);
  private Date timeInput;
  // required field
  private Date timeEstimatedFinish;
  // optional field
  private boolean isRecurring;
  // optional field
  private String recurringPeriodCronExpression;
  // required field
  private Long corporationId;
  // required field
  private List<Long> assignedStaffIdList;
  // required field
  private List<Long> managerIdList;

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

  public List<Long> getAssignedStaffIdList() {
    return assignedStaffIdList;
  }

  public void setAssignedStaffIdList(List<Long> assignedStaffIdList) {
    this.assignedStaffIdList = assignedStaffIdList;
  }

  public List<Long> getManagerIdList() {
    return managerIdList;
  }

  public void setManagerIdList(List<Long> managerIdList) {
    this.managerIdList = managerIdList;
  }
}