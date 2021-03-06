package com.walmart.labs.util;

import com.walmart.labs.domain.TaskTemplate;
import com.walmart.labs.exception.ExceptionFactory;
import com.walmart.labs.exception.ExceptionType;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Service;

/** How to user CronJobService: Step1 run init() */
@Service
public class CronJobService {
  private static final Logger logger = LoggerFactory.getLogger(CronJobService.class);
  /**
   * This 'map' variable is used for storing association relationship between task and
   * CronSequenceGenerator. Each task should only have one cron sequece generator.
   */
  private Map<Long, CronSequenceGenerator> map = new HashMap<>();

  /**
   * init() function will take a created/edited task as input. Extract recurring period cron job
   * expression and task id from task.
   *
   * @param taskTemplate taskTemplate will provide id and recurring period cron expression for the
   *     task if it is a recurring taskTemplate.
   */
  public void init(TaskTemplate taskTemplate) {
    String cronExpression = taskTemplate.getRecurringPeriodCronExpression();
    Long taskTemplateId = taskTemplate.getId();
    CronSequenceGenerator generator = new CronSequenceGenerator(cronExpression);
    if (map.containsKey(taskTemplateId)) {
      logger.info(
          String.format(
              "Updating cron sequence generator for task template id %s",
              Long.toString(taskTemplateId)));
    }
    map.put(taskTemplateId, generator);
  }

  /**
   * calculate the exact execution time for next running.
   *
   * @param taskTemplateId
   * @return
   */
  public Date calculateNextJobExecutionDate(Long taskTemplateId) {
    if (map.containsKey(taskTemplateId)) {
      CronSequenceGenerator generator = map.get(taskTemplateId);
      return generator.next(new Date());
    } else {
      throw ExceptionFactory.create(
          ExceptionType.IllegalArgumentException,
          "Invalid taskId provided for calculating next cron job execution date");
    }
  }

  /**
   * calculate cron job running period
   *
   * @param taskTemplateId
   * @return
   */
  public long calculatePeriod(Long taskTemplateId) {
    if (map.containsKey(taskTemplateId)) {
      CronSequenceGenerator generator = map.get(taskTemplateId);
      Date firstExecutionDate = generator.next(new Date());
      Date secondExecutionDate = generator.next(firstExecutionDate);
      return secondExecutionDate.getTime() - firstExecutionDate.getTime();
    } else {
      throw ExceptionFactory.create(
          ExceptionType.IllegalArgumentException,
          "Invalid taskId provided for calculating cron period");
    }
  }
}
