package com.walmart.labs.domain.mapping;

import com.walmart.labs.domain.TaskTemplate;
import com.walmart.labs.domain.User;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "task_template_staff_user_mapping")
public class TaskTemplateStaffUserMapping implements Serializable {
  private TaskTemplate taskTemplate;
  private User staff;

  @Id
  @ManyToOne
  @JoinColumn(name = "task_template_id")
  public TaskTemplate getTaskTemplate() {
    return taskTemplate;
  }

  public void setTaskTemplate(TaskTemplate taskTemplate) {
    this.taskTemplate = taskTemplate;
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
