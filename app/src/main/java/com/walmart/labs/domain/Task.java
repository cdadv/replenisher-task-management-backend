package com.walmart.labs.domain;

import java.util.Date;
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

@Entity
public class Task extends BasicDomain {
  /** task name */
  private String name;
  /**
   * {@link TaskStatus} are pre-defined enums for tracking status(progress) of tasks.
   *
   * <p>JsonDeserializer may be used when deserialize dto(in json format) request from frontend or
   * other backend
   */
  @Enumerated(EnumType.STRING)
  private TaskStatus taskStatus;

  /** {@link TaskPriority} are pre-defined enums for marking task priorities. */
  @Enumerated(EnumType.STRING)
  private TaskPriority taskPriority;
  /**
   * Claim that notes field is created as LongText type column in database for storing descriptions,
   * notes and feedback.
   */
  @Column(length = 65535, columnDefinition = "Text")
  @Type(type = "text")
  private String description;

  @Column(length = 65535, columnDefinition = "Text")
  @Type(type = "text")
  private String note;

  @Column(length = 65535, columnDefinition = "Text")
  @Type(type = "text")
  private String feedback;

  /**
   * Created task time for getting inputted priority. Created time is passed in and generated by
   * frontend.
   */
  private Date timeInput;

  /**
   * Estimated finish task time for getting time estimated to perform the task. Frontend may pass in
   * an estimated finish time in {@link Date} format. Backend will calculate the difference between
   * created time and finish time to get the duration. duration is in milli-seconds.
   *
   * <p>Date timeEstimatedFinish = DateUtils.addMilliseconds(timeInput.getTime(),
   * estimatedDuration);
   */
  private Date timeEstimatedFinish;

  @ManyToOne private Corporation corporation;

  /** List of staff that are assigned to this task */
  @ManyToMany
  @JoinTable(
    name = "task_staff_user_mapping",
    joinColumns = @JoinColumn(name = "task_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "staff_user_id", referencedColumnName = "id")
  )
  private Set<User> staffSet;

  /** List of manager that manage this task */
  @ManyToMany
  @JoinTable(
    name = "task_manager_user_mapping",
    joinColumns = @JoinColumn(name = "task_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "manager_user_id", referencedColumnName = "id")
  )
  private Set<User> managerSet;

  // TODO: consider add submitter and editor information either in here or user

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

  public TaskPriority getTaskPriority() {
    return taskPriority;
  }

  public void setTaskPriority(TaskPriority taskPriority) {
    this.taskPriority = taskPriority;
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
