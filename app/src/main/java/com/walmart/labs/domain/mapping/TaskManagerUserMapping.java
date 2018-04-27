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
@Table(name = "task_manager_user_mapping")
@IdClass(TaskManagerUserMapping.class)
public class TaskManagerUserMapping implements Serializable {
  private Task task;
  private User manager;

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
  @JoinColumn(name = "manager_user_id")
  public User getManager() {
    return manager;
  }

  public void setManager(User manager) {
    this.manager = manager;
  }
}
