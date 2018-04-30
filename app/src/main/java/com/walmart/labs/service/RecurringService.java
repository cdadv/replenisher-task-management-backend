package com.walmart.labs.service;

import com.walmart.labs.domain.TaskTemplate;
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

  public void createRecurringJob(User user, TaskTemplate taskTemplate) {
    cronJobService.init(taskTemplate);
    Date nextJobExecutionDate = cronJobService.calculateNextJobExecutionDate(taskTemplate.getId());
    long period = cronJobService.calculatePeriod(taskTemplate.getId());
    TimerJob timerJob = new TimerJob(taskTemplate, taskService);
    timerJobManager.startJob(
        timerJob, Long.toString(taskTemplate.getId()), nextJobExecutionDate, period);
  }

  public void updateRecurringJob(User user, TaskTemplate taskTemplatek) {
    deleteRecurringJob(user, taskTemplatek);
    createRecurringJob(user, taskTemplatek);
  }

  public void deleteRecurringJob(User user, TaskTemplate taskTemplate) {
    timerJobManager.stopJob(Long.toString(taskTemplate.getId()));
  }
}
