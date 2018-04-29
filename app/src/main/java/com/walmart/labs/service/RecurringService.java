package com.walmart.labs.service;

import com.walmart.labs.domain.Task;
import com.walmart.labs.domain.User;
import com.walmart.labs.job.TimerJob;
import com.walmart.labs.job.TimerJobManager;
import com.walmart.labs.util.CronJobService;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecurringService {
  @Autowired private TimerJobManager timerJobManager;
  @Autowired private TaskService taskService;
  @Autowired private CronJobService cronJobService;

  public void createRecurringJob(User user, Task task) {
    cronJobService.init(task);
    Date nextJobExecutionDate = cronJobService.calculateNextJobExecutionDate(task.getId());
    long period = cronJobService.calculatePeriod(task.getId());
    TimerJob timerJob = new TimerJob(task, taskService);
    timerJobManager.startJob(timerJob, Long.toString(task.getId()), nextJobExecutionDate, period);
  }

  public void updateRecurringJob(User user, Task task) {
    deleteRecurringJob(user, task);
    createRecurringJob(user, task);
  }

  public void deleteRecurringJob(User user, Task task) {
    timerJobManager.stopJob(Long.toString(task.getId()));
  }
}
