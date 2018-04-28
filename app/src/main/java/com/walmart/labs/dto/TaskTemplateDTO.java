package com.walmart.labs.dto;

import java.util.Set;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class TaskTemplateDTO {
  private Long taskTemplateId;
  private String name;
  private String description;
  private String note;
  private String taskPriorityString;
  private long estimatedDuration;
  private boolean isRecurring;
  private String recurringPeriodCronExpression;
  private Long corporationId;
  // required field for creating
  private Set<Long> assignedStaffIdSet;
  // required field for creating
  private Set<Long> managerIdSet;

  // following two fields will be ignored during deserialization
  @JsonIgnore
  private Set<UserDTO> assignedStaffUserDTOSet;
  @JsonIgnore
  private Set<UserDTO> managerUserDTOSet;

  public Long getTaskTemplateId() {
    return taskTemplateId;
  }

  public void setTaskTemplateId(Long taskTemplateId) {
    this.taskTemplateId = taskTemplateId;
  }

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

  public String getTaskPriorityString() {
    return taskPriorityString;
  }

  public void setTaskPriorityString(String taskPriorityString) {
    this.taskPriorityString = taskPriorityString;
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

  @JsonProperty
  public Set<UserDTO> getAssignedStaffUserDTOSet() {
    return assignedStaffUserDTOSet;
  }

  @JsonIgnore
  public void setAssignedStaffUserDTOSet(
      Set<UserDTO> assignedStaffUserDTOSet) {
    this.assignedStaffUserDTOSet = assignedStaffUserDTOSet;
  }

  @JsonProperty
  public Set<UserDTO> getManagerUserDTOSet() {
    return managerUserDTOSet;
  }

  @JsonIgnore
  public void setManagerUserDTOSet(Set<UserDTO> managerUserDTOSet) {
    this.managerUserDTOSet = managerUserDTOSet;
  }
}
