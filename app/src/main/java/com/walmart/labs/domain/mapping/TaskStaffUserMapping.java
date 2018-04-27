package com.walmart.labs.domain.mapping;

import com.walmart.labs.domain.Task;
import com.walmart.labs.domain.User;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "task_staff_user_mapping")
@IdClass(TaskStaffUserMapping.class)
public class TaskStaffUserMapping implements Serializable {
  private Task task;
  private User staff;

  @Id
  @ManyToOne
  @JoinColumn(name = "task_id")
  public Task getTask() {
    return task;
  }

  public void setTask(Task task) {
    this.task = task;
  }

  @Id
  @ManyToOne
  @JoinColumn(name = "staff_user_id")
  public User getStaff() {
    return staff;
  }

  public void setStaff(User staff) {
    this.staff = staff;
  }
}
