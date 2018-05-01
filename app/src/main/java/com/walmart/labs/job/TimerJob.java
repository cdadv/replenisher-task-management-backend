package com.walmart.labs.job;

import com.walmart.labs.domain.TaskTemplate;
import com.walmart.labs.service.TaskService;
import java.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimerJob extends TimerTask {
  private static final Logger logger = LoggerFactory.getLogger(TimerJob.class);

  private TaskTemplate newTaskTemplate;
  private TaskService taskService;

  public TimerJob(TaskTemplate taskTemplate, TaskService taskService) {
    TaskTemplate newTaskTemplate = new TaskTemplate();
    newTaskTemplate.setName(taskTemplate.getName());
    newTaskTemplate.setDescription(taskTemplate.getDescription());
    newTaskTemplate.setNote(taskTemplate.getNote());
    newTaskTemplate.setEstimatedDuration(taskTemplate.getEstimatedDuration());
    newTaskTemplate.setTaskPriority(taskTemplate.getTaskPriority());
    newTaskTemplate.setCorporation(taskTemplate.getCorporation());
    newTaskTemplate.setStaffSet(taskTemplate.getStaffSet());
    newTaskTemplate.setManagerSet(taskTemplate.getManagerSet());
    newTaskTemplate.setRecurring(taskTemplate.isRecurring());
    newTaskTemplate.setRecurringPeriodCronExpression(
        taskTemplate.getRecurringPeriodCronExpression());
    this.newTaskTemplate = newTaskTemplate;
    this.taskService = taskService;
  }

  /** scheduled job will run periodically to create a task in based on */
  @Override
  public void run() {
    logger.info("Recurring job is running");
    taskService.createTaskFromTaskTemplate(newTaskTemplate);
  }
}
