package com.walmart.labs.controller;

import com.walmart.labs.dto.ResponseDTO;
import com.walmart.labs.dto.TaskDTO;
import com.walmart.labs.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskController {
  @Autowired private TaskService taskService;

  /**
   * Create a task and store in database. Needs dto object in the request body.
   *
   * <p>Request URL: /task
   *
   * <p>Request Method: POST
   *
   * @param taskDTO request body {@link TaskDTO} contains task name, task status, task description,
   *     task note, task feedback, the time when user click create task button (timeInput),
   *     estimated finish time, isRecurring indicates if this is a recurring tasks,
   *     recurringPeriodCronExpression means if this task is a recurring task (isRecurring = true)
   *     the period cron expression will help to set up the recurring period, corporationId
   *     indicates which corporation this task belongs to, assignedStaffIdList means a list of staff
   *     user id the task assinged to, managerIdList means a list of manager user id were set as
   *     reporter(manager).
   * @return Empty result with success/fail messages.
   */
  @RequestMapping(value = "/task", method = RequestMethod.POST)
  public ResponseEntity<ResponseDTO<Void>> createQuote(@RequestBody TaskDTO taskDTO) {
    ResponseDTO<Void> responseDTO = new ResponseDTO<>();

    taskService.createTask(taskDTO);
    responseDTO.addMessage("Created a task successfully!");
    responseDTO.setSuccess(true);
    return new ResponseEntity<>(responseDTO, HttpStatus.OK);
  }
}
