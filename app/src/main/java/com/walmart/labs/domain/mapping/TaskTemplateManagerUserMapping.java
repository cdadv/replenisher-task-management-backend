package com.walmart.labs.domain.mapping;

import com.walmart.labs.domain.TaskTemplate;
import com.walmart.labs.domain.User;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "task_template_manager_user_mapping")
@IdClass(TaskTemplateManagerUserMapping.class)
public class TaskTemplateManagerUserMapping implements Serializable {
  private TaskTemplate taskTemplate;
  private User manager;

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
  @JoinColumn(name = "manager_user_id")
  public User getManager() {
    return manager;
  }

  public void setManager(User manager) {
    this.manager = manager;
  }
}
