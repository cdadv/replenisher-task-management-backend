package com.walmart.labs.job;

import static java.lang.Math.toIntExact;

import com.walmart.labs.domain.Task;
import com.walmart.labs.domain.TaskStatus;
import com.walmart.labs.service.TaskService;
import java.util.Date;
import java.util.TimerTask;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimerJob extends TimerTask {
  private static final Logger logger = LoggerFactory.getLogger(TimerJob.class);

  private Task newTask;
  private TaskService taskService;

  public TimerJob(Task task, TaskService taskService) {
    Task newTask = new Task();
    if (newTask.getManagerSet() == null
        || newTask.getManagerSet().isEmpty()
        || newTask.getStaffSet() == null
        || newTask.getStaffSet().isEmpty()) {
      newTask.setTaskStatus(TaskStatus.CREATED);
    } else {
      newTask.setTaskStatus(TaskStatus.ASSIGNED);
    }
    newTask.setTimeInput(new Date());
    Date timeEstimatedFinish =
        DateUtils.addMilliseconds(
            newTask.getTimeInput(),
            toIntExact(task.getTimeEstimatedFinish().getTime() - task.getTimeInput().getTime()));
    newTask.setTimeEstimatedFinish(timeEstimatedFinish);
    newTask.setName(task.getName());
    newTask.setTaskPriority(task.getTaskPriority());
    newTask.setTaskStatus(task.getTaskStatus());
    newTask.setDescription(task.getDescription());
    newTask.setFeedback(task.getFeedback());
    newTask.setCorporation(task.getCorporation());
    newTask.setNote(task.getNote());
    newTask.setRecurring(task.isRecurring());
    newTask.setRecurringPeriodCronExpression(task.getRecurringPeriodCronExpression());
    newTask.setStaffSet(task.getStaffSet());
    newTask.setManagerSet(task.getManagerSet());
    this.newTask = newTask;
    this.taskService = taskService;
  }

  /** scheduled job will run periodically to create a task in based on */
  @Override
  public void run() {
    logger.info("Recurring job is running");
    taskService.createTask(newTask);
  }
}
