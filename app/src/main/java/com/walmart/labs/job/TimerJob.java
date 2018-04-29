package com.walmart.labs.job;

import static java.lang.Math.toIntExact;

import com.walmart.labs.domain.Task;
import com.walmart.labs.domain.TaskStatus;
import com.walmart.labs.service.TaskService;
import java.util.Date;
import java.util.TimerTask;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class TimerJob extends TimerTask {
  @Autowired private TaskService taskService;

  private Task newTask;

  public TimerJob(Task task) {
    Task newTask = new Task();
    BeanUtils.copyProperties(newTask, task);
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
    this.newTask = newTask;
  }

  /** scheduled job will run periodically to create a task in based on */
  @Override
  public void run() {
    taskService.createTask(newTask);
  }
}
