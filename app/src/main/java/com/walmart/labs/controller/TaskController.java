package com.walmart.labs.controller;

import com.walmart.labs.domain.User;
import com.walmart.labs.dto.ResponseDTO;
import com.walmart.labs.dto.TaskDTO;
import com.walmart.labs.service.TaskService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskController {
  @Autowired private TaskService taskService;

  /**
   * Get task list from database based on user's role. This endpoint does not need any inputs (only
   * the access_token). Backend will get the user information from the access_token and query all
   * the tasks that the user is authorized to query.
   *
   * <p>Request URL: /task
   *
   * <p>Request Method: GET
   *
   * @return List of TaskDTO that the user is authorized to query.
   */
  @RequestMapping(value = "/task", method = RequestMethod.GET)
  public ResponseEntity<ResponseDTO<List<TaskDTO>>> getTaskListByUser() {
    ResponseDTO<List<TaskDTO>> responseDTO = new ResponseDTO<>();

    OAuth2Authentication authentication =
        (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
    User user = (User) authentication.getPrincipal();

    List<TaskDTO> taskDTOList = taskService.getTaskList(user);
    responseDTO.addMessage("Get task list by user successfully!");
    responseDTO.setSuccess(true);
    responseDTO.setResult(taskDTOList);
    return new ResponseEntity<>(responseDTO, HttpStatus.OK);
  }

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
  public ResponseEntity<ResponseDTO<Void>> createTask(@RequestBody TaskDTO taskDTO) {
    ResponseDTO<Void> responseDTO = new ResponseDTO<>();

    OAuth2Authentication authentication =
        (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
    User user = (User) authentication.getPrincipal();

    taskService.createTask(user, taskDTO);
    responseDTO.addMessage("Created a task successfully!");
    responseDTO.setSuccess(true);
    return new ResponseEntity<>(responseDTO, HttpStatus.OK);
  }


}
