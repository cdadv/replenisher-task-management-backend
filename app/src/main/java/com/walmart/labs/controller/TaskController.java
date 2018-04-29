package com.walmart.labs.controller;

import com.walmart.labs.domain.User;
import com.walmart.labs.dto.ResponseDTO;
import com.walmart.labs.dto.TaskDTO;
import com.walmart.labs.dto.TaskTemplateDTO;
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
import org.springframework.web.bind.annotation.RequestParam;
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
  public ResponseEntity<ResponseDTO<List<TaskDTO>>> getTaskList() {
    ResponseDTO<List<TaskDTO>> responseDTO = new ResponseDTO<>();

    OAuth2Authentication authentication =
        (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
    User user = (User) authentication.getPrincipal();

    List<TaskDTO> taskDTOList = taskService.getTaskDTOList(user);
    responseDTO.addMessage("Get task list by user successfully!");
    responseDTO.setSuccess(true);
    responseDTO.setResult(taskDTOList);
    return new ResponseEntity<>(responseDTO, HttpStatus.OK);
  }

  @RequestMapping(value = "/task/sort/rank", method = RequestMethod.GET)
  public ResponseEntity<ResponseDTO<List<TaskDTO>>> getTaskListSortByRank() {
    ResponseEntity<ResponseDTO<List<TaskDTO>>> responseEntity = getTaskList();
    ResponseDTO<List<TaskDTO>> responseDTO = responseEntity.getBody();
    if (!responseDTO.getSuccess() || responseDTO.getResult().size() <= 1) {
      return responseEntity;
    }
    List<TaskDTO> taskDTOListSortedById = responseDTO.getResult();
    List<TaskDTO> taskDTOListSortedByRank =
        taskService.sortTaskDTOListByRank(taskDTOListSortedById);
    responseDTO.setResult(taskDTOListSortedByRank);
    responseDTO.addMessage("Get task list sorted by rank");
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

  /**
   * Update a existing task and save back to database. Needs dto object in the request body.
   *
   * <p>Request URL: /task
   *
   * <p>Request Method: PUT
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
  @RequestMapping(value = "/task", method = RequestMethod.PUT)
  public ResponseEntity<ResponseDTO<Void>> updateTask(@RequestBody TaskDTO taskDTO) {
    ResponseDTO<Void> responseDTO = new ResponseDTO<>();

    OAuth2Authentication authentication =
        (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
    User user = (User) authentication.getPrincipal();

    taskService.updateTask(user, taskDTO);
    responseDTO.addMessage("Updated a task successfully!");
    responseDTO.setSuccess(true);
    return new ResponseEntity<>(responseDTO, HttpStatus.OK);
  }

  /**
   * Delete a existing task by provided task id.
   *
   * <p>Request URL: /task
   *
   * <p>Request Method: DELETE
   *
   * @param taskId taskId should be an id for an existing task record in database.
   * @return Empty result with success/fail messages.
   */
  @RequestMapping(value = "/task", method = RequestMethod.DELETE)
  public ResponseEntity<ResponseDTO<Void>> deleteTask(@RequestParam Long taskId) {
    ResponseDTO<Void> responseDTO = new ResponseDTO<>();

    OAuth2Authentication authentication =
        (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
    User user = (User) authentication.getPrincipal();

    taskService.deleteTask(user, taskId);
    responseDTO.addMessage("Deleted a task successfully!");
    responseDTO.setSuccess(true);
    return new ResponseEntity<>(responseDTO, HttpStatus.OK);
  }

  /**
   * Get task template from database based on user's role. This endpoint does not need any inputs
   * (only the access_token). Backend will get the user information from the access_token and query
   * all the tasks that the user is authorized to query.
   *
   * <p>Request URL: /task/template
   *
   * <p>Request Method: GET
   *
   * @return List of TaskTemplate that the user is authorized to check
   */
  @RequestMapping(value = "/task/template", method = RequestMethod.GET)
  public ResponseEntity<ResponseDTO<List<TaskTemplateDTO>>> getTaskTemplateList() {
    ResponseDTO<List<TaskTemplateDTO>> responseDTO = new ResponseDTO<>();

    OAuth2Authentication authentication =
        (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
    User user = (User) authentication.getPrincipal();

    List<TaskTemplateDTO> taskTemplateDTOList = taskService.getTaskTemplateDTOList(user);
    responseDTO.addMessage("Get task template list by user successfully!");
    responseDTO.setSuccess(true);
    responseDTO.setResult(taskTemplateDTOList);
    return new ResponseEntity<>(responseDTO, HttpStatus.OK);
  }

  /**
   * Create task template based on TaskDTO! This endpoint is mainly used for frontend to create task
   * template in the task creation page. This endpoint needs user fill out all the information for
   * creating a 'Task'. the service will extract information from taskDTO and save them as
   * TaskTemplate
   *
   * <p>Request URL: /task/template
   *
   * <p>Request Method: POST
   *
   * @param taskDTO taskDTO contains redundant information such as TackStatus, feedback. But
   *     contains every fields in TaskTemplate. So we can reuse this to receive request from
   *     frontend.
   * @return Empty result with success/fail messages.
   */
  @RequestMapping(value = "/task/template", method = RequestMethod.POST)
  public ResponseEntity<ResponseDTO<Void>> createTaskTemplate(@RequestBody TaskDTO taskDTO) {
    ResponseDTO<Void> responseDTO = new ResponseDTO<>();

    OAuth2Authentication authentication =
        (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
    User user = (User) authentication.getPrincipal();

    taskService.createTaskTemplate(user, taskDTO);
    responseDTO.addMessage("Created a task template successfully!");
    responseDTO.setSuccess(true);
    return new ResponseEntity<>(responseDTO, HttpStatus.OK);
  }

  /**
   * Update task template by taskTemplateDTO.
   *
   * <p>Request URL: /task/template
   *
   * <p>Request Method: PUT
   *
   * @param taskTemplateDTO {@link TaskDTO} contains similar information. taskTemplateId is required
   *     field. Other fields are optional, Fields that presented will be updated and saved in
   *     database.
   * @return Empty result with success/fail messages.
   */
  @RequestMapping(value = "/task/template", method = RequestMethod.PUT)
  public ResponseEntity<ResponseDTO<Void>> updateTaskTemplate(
      @RequestBody TaskTemplateDTO taskTemplateDTO) {
    ResponseDTO<Void> responseDTO = new ResponseDTO<>();

    OAuth2Authentication authentication =
        (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
    User user = (User) authentication.getPrincipal();

    taskService.updateTaskTemplate(user, taskTemplateDTO);
    responseDTO.addMessage("Updated a task template successfully!");
    responseDTO.setSuccess(true);
    return new ResponseEntity<>(responseDTO, HttpStatus.OK);
  }

  /**
   * Delete existing task template from database according to provided taskTemplateId
   *
   * @param taskTemplateId delete the records in database with taskTemplateId
   * @return Empty result with success/fail messages.
   */
  @RequestMapping(value = "/task/template", method = RequestMethod.DELETE)
  public ResponseEntity<ResponseDTO<Void>> updateTask(@RequestParam Long taskTemplateId) {
    ResponseDTO<Void> responseDTO = new ResponseDTO<>();

    OAuth2Authentication authentication =
        (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
    User user = (User) authentication.getPrincipal();

    taskService.deleteTaskTemplate(user, taskTemplateId);
    responseDTO.addMessage("Deleted a task template successfully!");
    responseDTO.setSuccess(true);
    return new ResponseEntity<>(responseDTO, HttpStatus.OK);
  }
}
