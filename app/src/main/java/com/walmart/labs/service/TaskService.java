package com.walmart.labs.service;

import com.walmart.labs.domain.Corporation;
import com.walmart.labs.domain.Task;
import com.walmart.labs.domain.TaskTemplate;
import com.walmart.labs.domain.User;
import com.walmart.labs.domain.UserRole;
import com.walmart.labs.domain.mapping.TaskManagerUserMapping;
import com.walmart.labs.domain.mapping.TaskStaffUserMapping;
import com.walmart.labs.dto.TaskDTO;
import com.walmart.labs.dto.TaskTemplateDTO;
import com.walmart.labs.dto.UserDTO;
import com.walmart.labs.exception.ExceptionFactory;
import com.walmart.labs.exception.ExceptionType;
import com.walmart.labs.repository.TaskRepository;
import com.walmart.labs.repository.TaskTemplateRepository;
import com.walmart.labs.repository.mapping.TaskManagerUserMappingRepository;
import com.walmart.labs.repository.mapping.TaskStaffUserMappingRepository;
import com.walmart.labs.util.ValidationService;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TaskService {
  private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

  @Autowired private TaskRepository taskRepository;
  @Autowired private TaskTemplateRepository taskTemplateRepository;
  @Autowired private ValidationService validationService;
  @Autowired private TaskStaffUserMappingRepository taskStaffUserMappingRepository;
  @Autowired private TaskManagerUserMappingRepository taskManagerUserMappingRepository;

  /**
   * Query all allowed task based on user's role. And extract all the information from Task to
   * TaskDTO.
   *
   * @param user user is used for determining which tasks to return.
   * @return list of TaskDTO
   */
  public List<TaskDTO> getTaskDTOList(User user) {
    List<TaskDTO> taskDTOList = new ArrayList<>();
    List<Task> taskList = null;
    Set<UserRole> allowedRoleSet = user.getAllowedRoleSet();
    for (UserRole role : allowedRoleSet) {
      switch (role.getName()) {
          // Assuming there are only three roles supported in backend.
        case "ROLE_ADMIN":
          taskList = getTaskIdListForAdmin(user);
          break;
        case "ROLE_USER_MANAGER":
          taskList = getTaskIdListForManager(user);
          break;
        case "ROLE_USER_STAFF":
          taskList = getTaskIdListForStaff(user);
          break;
        default:
          throw ExceptionFactory.create(
              ExceptionType.IllegalRequestBodyFieldsException,
              String.format("Detected invalid role for user: %s", role.getName()));
      }
    }
    if (taskList == null || taskList.isEmpty()) {
      return new ArrayList<>();
    }
    taskDTOList = mapTaskListToTaskDTOList(taskList, taskDTOList);
    logger.info("Queried a task successfully!");
    return taskDTOList;
  }

  public List<Task> getTaskList(List<Long> taskIdList) {
    return taskRepository.findAllById(taskIdList);
  }

  /**
   * Query task template list based on user's role. And then map list of task template to list of
   * task template dto.
   *
   * @param user
   * @return list of TaskTemplateDTO
   */
  public List<TaskTemplateDTO> getTaskTemplateDTOList(User user) {
    List<TaskTemplateDTO> taskTemplateDTOList = new ArrayList<>();
    List<TaskTemplate> taskTemplateList = null;
    Set<UserRole> allowedRoleSet = user.getAllowedRoleSet();
    for (UserRole role : allowedRoleSet) {
      switch (role.getName()) {
          // Assuming there are only three roles supported in backend.
        case "ROLE_ADMIN":
          taskTemplateList = taskTemplateRepository.findAll();
          break;
        case "ROLE_USER_MANAGER":
          // TODO: simplified querying process for task template. query all by corporation for now.
          // (query by creator of task templates)
          taskTemplateList = taskTemplateRepository.findAllByCorporation(user.getCorporation());
          break;
        case "ROLE_USER_STAFF":
          throw ExceptionFactory.create(
              ExceptionType.IllegalRequestBodyFieldsException,
              "Detected invalid role for user: staff user is not authorized to query task template list");
        default:
          throw ExceptionFactory.create(
              ExceptionType.IllegalRequestBodyFieldsException,
              String.format("Detected invalid role for user: %s", role.getName()));
      }
    }
    if (taskTemplateList == null || taskTemplateList.isEmpty()) {
      return new ArrayList<>();
    }
    taskTemplateDTOList =
        mapTaskTemplateListToTaskTemplateDTOList(taskTemplateList, taskTemplateDTOList);
    logger.info("Queried a task template successfully!");
    return taskTemplateDTOList;
  }

  /**
   * Extract all the information from TaskDTO to Task and then save.
   *
   * @param taskDTO TaskDTO passed from frontend or client.
   */
  public void createTask(User user, TaskDTO taskDTO) {
    Task task = new Task();
    task = mapTaskDTOToTask(user, taskDTO, task);
    createTask(task);
    logger.info("Created a task successfully!");
  }

  public void createTask(Task task) {
    taskRepository.save(task);
  }

  /**
   * Extract all the information from TaskDTO to Task first and map Task to TaskTemplate, then save.
   *
   * @param user
   * @param taskDTO
   */
  public void createTaskTemplate(User user, TaskDTO taskDTO) {
    Task task = new Task();
    task = mapTaskDTOToTask(user, taskDTO, task);
    TaskTemplate taskTemplate = new TaskTemplate();
    taskTemplate = mapTaskToTaskTemplate(task, taskTemplate);
    createTaskTemplate(taskTemplate);
    logger.info("Created a task template successfully!");
  }

  public void createTaskTemplate(TaskTemplate taskTemplate) {
    taskTemplateRepository.save(taskTemplate);
  }

  public void updateTask(User user, TaskDTO taskDTO) {
    Long taskId = taskDTO.getTaskId();
    Task task = validationService.validateTaskIdForUpdatingOrDeletingForAdmin(taskId, user);
    task = mapTaskDTOToTask(user, taskDTO, task);
    updateTask(task);
    logger.info("Updated a task successfully!");
  }

  public void updateTask(Task task) {
    taskRepository.save(task);
  }

  public void updateTaskTemplate(User user, TaskTemplateDTO taskTemplateDTO) {
    Long taskTemplateId = taskTemplateDTO.getTaskTemplateId();
    TaskTemplate taskTemplate =
        validationService.validateTaskTemplateIdForUpdatingOrDeletingForAdmin(taskTemplateId, user);
    taskTemplate = mapTaskTemplateDTOToTaskTemplate(user, taskTemplateDTO, taskTemplate);
    updateTaskTemplate(taskTemplate);
    logger.info("Updated a task template successfully!");
  }

  public void updateTaskTemplate(TaskTemplate taskTemplate) {
    taskTemplateRepository.save(taskTemplate);
  }

  public void deleteTask(User user, Long taskId) {
    Task task = null;
    Set<UserRole> allowedRoleSet = user.getAllowedRoleSet();
    for (UserRole role : allowedRoleSet) {
      switch (role.getName()) {
          // Assuming there are only three roles supported in backend.
        case "ROLE_ADMIN":
          task = validationService.validateTaskIdForUpdatingOrDeletingForAdmin(taskId, user);
          break;
        case "ROLE_USER_MANAGER":
          task = validationService.validateTaskIdForUpdatingOrDeletingForManager(taskId, user);
          break;
        case "ROLE_USER_STAFF":
          task = validationService.validateTaskIdForUpdatingOrDeletingForManager(taskId, user);
          break;
        default:
          throw ExceptionFactory.create(
              ExceptionType.IllegalRequestBodyFieldsException,
              String.format("Detected invalid role for user: %s", role.getName()));
      }
    }
    deleteTask(task);
    logger.info("Deleted a task successfully!");
  }

  public void deleteTask(Task task) {
    taskRepository.delete(task);
  }

  public void deleteTaskTemplate(User user, Long taskTemplateId) {
    TaskTemplate taskTemplate = null;
    Set<UserRole> allowedRoleSet = user.getAllowedRoleSet();
    for (UserRole role : allowedRoleSet) {
      switch (role.getName()) {
          // Assuming there are only three roles supported in backend.
        case "ROLE_ADMIN":
          taskTemplate =
              validationService.validateTaskTemplateIdForUpdatingOrDeletingForAdmin(
                  taskTemplateId, user);
          break;
        case "ROLE_USER_MANAGER":
          taskTemplate =
              validationService.validateTaskTemplateIdForUpdatingOrDeletingForManager(
                  taskTemplateId, user);
          break;
        case "ROLE_USER_STAFF":
          throw ExceptionFactory.create(
              ExceptionType.IllegalRequestBodyFieldsException,
              "Detected invalid role for user: staff user is not authorized to delete task template.");
        default:
          throw ExceptionFactory.create(
              ExceptionType.IllegalRequestBodyFieldsException,
              String.format("Detected invalid role for user: %s", role.getName()));
      }
    }
    deleteTaskTemplate(taskTemplate);
    logger.info("Deleted a task template successfully!");
  }

  public void deleteTaskTemplate(TaskTemplate taskTemplate) {
    taskTemplateRepository.delete(taskTemplate);
  }

  private TaskTemplate mapTaskTemplateDTOToTaskTemplate(
      User currentUser, TaskTemplateDTO taskTemplateDTO, TaskTemplate taskTemplate) {
    if (taskTemplate == null) {
      taskTemplate = new TaskTemplate();
    }
    Set<UserRole> allowedRoleSet = currentUser.getAllowedRoleSet();

    String name = null;
    String taskPriorityString = null;
    String description = null;
    String note = null;
    long estimatedDuration = 0;
    boolean isRecurring = false;
    String recurringPeriodCronExpression = null;
    Long corporationId = null;
    Corporation corporation = null;
    Set<Long> assignedStaffIdSet = null;
    Set<Long> managerIdSet = null;

    for (UserRole role : allowedRoleSet) {
      switch (role.getName()) {
          // Assuming there are only three roles supported in backend.
        case "ROLE_ADMIN":
          validationService.validateTaskTemplateDTOAndSetForAdmin(taskTemplateDTO, taskTemplate);
          // REQUIRED field for task: 1. extract task name
          name = taskTemplateDTO.getName();
          validationService.validateTaskNameForAdmin(name, taskTemplate);
          // OPTIONAL field for task: 3 extract task priority
          taskPriorityString = taskTemplateDTO.getTaskPriorityString();
          validationService.validateTaskPriorityStringAndSetForAdmin(
              taskPriorityString, taskTemplate);
          // OPTIONAL field for task: 4. extract task descriptions
          description = taskTemplateDTO.getDescription();
          validationService.validateTaskDescriptionAndSetForAdmin(description, taskTemplate);
          // OPTIONAL field for task: 5. extract task note
          note = taskTemplateDTO.getNote();
          validationService.validateTaskNoteAndSetForAdmin(note, taskTemplate);
          // REQUIRED field for task: 7. extract task input time (when the task is created by the
          // user.)
          estimatedDuration = taskTemplateDTO.getEstimatedDuration();
          validationService.validateTaskEstimatedDurationAndSetForAdmin(
              estimatedDuration, taskTemplate);
          // OPTIONAL field for task: 9. extract if the task is recurring task or not.
          isRecurring = taskTemplateDTO.isRecurring();
          recurringPeriodCronExpression = taskTemplateDTO.getRecurringPeriodCronExpression();
          validationService
              .validateTaskIsrecurringAndTaskRecurringPeriodCronExpressionAndSetForAdmin(
                  isRecurring, recurringPeriodCronExpression, taskTemplate);
          // REQUIRED field for task: 10. extract corporation from provided corporationId.
          corporationId = taskTemplateDTO.getCorporationId();
          corporation =
              validationService.validateTaskCorporationIdAndSetForAdmin(
                  corporationId, currentUser, taskTemplate);
          // REQUIRED field for task: 11. extract assigned staff list for this task from provided
          // assigned staff id list.
          assignedStaffIdSet = taskTemplateDTO.getAssignedStaffIdSet();
          validationService.validateTaskAssignedStaffIdSetAndSetForAdmin(
              assignedStaffIdSet, corporation, currentUser, taskTemplate);
          // REQUIRED field for task: 12. extract assigned manager list for this task from provided
          // manager id list.
          managerIdSet = taskTemplateDTO.getManagerIdSet();
          validationService.validateTaskManagerIdSetAndSetForAdmin(
              managerIdSet, corporation, currentUser, taskTemplate);
          break;
        case "ROLE_USER_MANAGER":
          validationService.validateTaskTemplateDTOAndSetForAdmin(taskTemplateDTO, taskTemplate);
          // REQUIRED field for task: 1. extract task name
          name = taskTemplateDTO.getName();
          validationService.validateTaskNameForAdmin(name, taskTemplate);
          // OPTIONAL field for task: 3 extract task priority
          taskPriorityString = taskTemplateDTO.getTaskPriorityString();
          validationService.validateTaskPriorityStringAndSetForAdmin(
              taskPriorityString, taskTemplate);
          // OPTIONAL field for task: 4. extract task descriptions
          description = taskTemplateDTO.getDescription();
          validationService.validateTaskDescriptionAndSetForAdmin(description, taskTemplate);
          // OPTIONAL field for task: 5. extract task note
          note = taskTemplateDTO.getNote();
          validationService.validateTaskNoteAndSetForAdmin(note, taskTemplate);
          // REQUIRED field for task: 7. extract task input time (when the task is created by the
          // user.)
          estimatedDuration = taskTemplateDTO.getEstimatedDuration();
          validationService.validateTaskEstimatedDurationAndSetForAdmin(
              estimatedDuration, taskTemplate);
          // OPTIONAL field for task: 9. extract if the task is recurring task or not.
          isRecurring = taskTemplateDTO.isRecurring();
          recurringPeriodCronExpression = taskTemplateDTO.getRecurringPeriodCronExpression();
          validationService
              .validateTaskIsrecurringAndTaskRecurringPeriodCronExpressionAndSetForAdmin(
                  isRecurring, recurringPeriodCronExpression, taskTemplate);
          // REQUIRED field for task: 10. extract corporation from provided corporationId.
          corporationId = taskTemplateDTO.getCorporationId();
          corporation =
              validationService.validateTaskCorporationIdAndSetForAdmin(
                  corporationId, currentUser, taskTemplate);
          // REQUIRED field for task: 11. extract assigned staff list for this task from provided
          // assigned staff id list.
          assignedStaffIdSet = taskTemplateDTO.getAssignedStaffIdSet();
          validationService.validateTaskAssignedStaffIdSetAndSetForAdmin(
              assignedStaffIdSet, corporation, currentUser, taskTemplate);
          // REQUIRED field for task: 12. extract assigned manager list for this task from provided
          // manager id list.
          managerIdSet = taskTemplateDTO.getManagerIdSet();
          validationService.validateTaskManagerIdSetAndSetForAdmin(
              managerIdSet, corporation, currentUser, taskTemplate);
          break;
        case "ROLE_USER_STAFF":
          throw ExceptionFactory.create(
              ExceptionType.IllegalRequestBodyFieldsException,
              "Detected invalid role for user: staff user is not authorized to update task template");
        default:
          throw ExceptionFactory.create(
              ExceptionType.IllegalRequestBodyFieldsException,
              String.format("Detected invalid role for user: %s", role.getName()));
      }
    }
    return taskTemplate;
  }

  private Task mapTaskDTOToTask(User currentUser, TaskDTO taskDTO, Task task) {
    if (task == null) {
      task = new Task();
    }

    Set<UserRole> allowedRoleSet = currentUser.getAllowedRoleSet();
    String name = null;
    String taskStatusString = null;
    String taskPriorityString = null;
    String description = null;
    String note = null;
    String feedback = null;
    Date timeInput = null;
    Date timeEstimatedFinish = null;
    boolean isRecurring = false;
    String recurringPeriodCronExpression = null;
    Long corporationId = null;
    Corporation corporation = null;
    Set<Long> assignedStaffIdSet = null;
    Set<Long> managerIdSet = null;

    // TODO: assuming there is only one role associate to a user
    for (UserRole role : allowedRoleSet) {
      switch (role.getName()) {
          // Assuming there are only three roles supported in backend.
        case "ROLE_ADMIN":
          validationService.validateTaskDTOAndSetForAdmin(taskDTO, task);
          // REQUIRED field for task: 1. extract task name
          name = taskDTO.getName();
          validationService.validateTaskNameForAdmin(name, task);
          // REQUIRED field for task: 2. extract task status
          taskStatusString = taskDTO.getTaskStatusString();
          validationService.validateTaskStatusStringAndSetForAdmin(taskStatusString, task);
          // OPTIONAL field for task: 3 extract task priority
          taskPriorityString = taskDTO.getTaskPriorityString();
          validationService.validateTaskPriorityStringAndSetForAdmin(taskPriorityString, task);
          // OPTIONAL field for task: 4. extract task descriptions
          description = taskDTO.getDescription();
          validationService.validateTaskDescriptionAndSetForAdmin(description, task);
          // OPTIONAL field for task: 5. extract task note
          note = taskDTO.getNote();
          validationService.validateTaskNoteAndSetForAdmin(note, task);
          // OPTIONAL field for task: 6. extract task feedback
          feedback = taskDTO.getFeedback();
          validationService.validateTaskFeedbackAndSetForAdmin(feedback, task);
          // REQUIRED field for task: 7. extract task input time (when the task is created by the
          // user.)
          timeInput = taskDTO.getTimeInput();
          validationService.validateTaskTimeInputAndSetForAdmin(timeInput, task);
          // REQUIRED field for task: 8. extract task estimated finish time and calculate task
          // estimated durations.
          timeEstimatedFinish = taskDTO.getTimeEstimatedFinish();
          validationService.validateTaskTimeEstimatedFinishAndTaskTimeInputAndSetForAdmin(
              timeEstimatedFinish, timeInput, task);
          // OPTIONAL field for task: 9. extract if the task is recurring task or not.
          isRecurring = taskDTO.isRecurring();
          recurringPeriodCronExpression = taskDTO.getRecurringPeriodCronExpression();
          validationService
              .validateTaskIsrecurringAndTaskRecurringPeriodCronExpressionAndSetForAdmin(
                  isRecurring, recurringPeriodCronExpression, task);
          // REQUIRED field for task: 10. extract corporation from provided corporationId.
          corporationId = taskDTO.getCorporationId();
          corporation =
              validationService.validateTaskCorporationIdAndSetForAdmin(
                  corporationId, currentUser, task);
          // REQUIRED field for task: 11. extract assigned staff list for this task from provided
          // assigned staff id list.
          assignedStaffIdSet = taskDTO.getAssignedStaffIdSet();
          validationService.validateTaskAssignedStaffIdSetAndSetForAdmin(
              assignedStaffIdSet, corporation, currentUser, task);
          // REQUIRED field for task: 12. extract assigned manager list for this task from provided
          // manager id list.
          managerIdSet = taskDTO.getManagerIdSet();
          validationService.validateTaskManagerIdSetAndSetForAdmin(
              managerIdSet, corporation, currentUser, task);
          break;
        case "ROLE_USER_MANAGER":
          validationService.validateTaskDTOAndSetForAdmin(taskDTO, task);
          // REQUIRED field for task: 1. extract task name
          name = taskDTO.getName();
          validationService.validateTaskNameForAdmin(name, task);
          // REQUIRED field for task: 2. extract task status
          taskStatusString = taskDTO.getTaskStatusString();
          validationService.validateTaskStatusStringAndSetForAdmin(taskStatusString, task);
          // OPTIONAL field for task: 3 extract task priority
          taskPriorityString = taskDTO.getTaskPriorityString();
          validationService.validateTaskPriorityStringAndSetForAdmin(taskPriorityString, task);
          // OPTIONAL field for task: 4. extract task descriptions
          description = taskDTO.getDescription();
          validationService.validateTaskDescriptionAndSetForAdmin(description, task);
          // OPTIONAL field for task: 5. extract task note
          note = taskDTO.getNote();
          validationService.validateTaskNoteAndSetForAdmin(note, task);
          // OPTIONAL field for task: 6. extract task feedback
          feedback = taskDTO.getFeedback();
          validationService.validateTaskFeedbackAndSetForAdmin(feedback, task);
          // REQUIRED field for task: 7. extract task input time (when the task is created by the
          // user.)
          timeInput = taskDTO.getTimeInput();
          validationService.validateTaskTimeInputAndSetForAdmin(timeInput, task);
          // REQUIRED field for task: 8. extract task estimated finish time and calculate task
          // estimated durations.
          timeEstimatedFinish = taskDTO.getTimeEstimatedFinish();
          validationService.validateTaskTimeEstimatedFinishAndTaskTimeInputAndSetForAdmin(
              timeEstimatedFinish, timeInput, task);
          // OPTIONAL field for task: 9. extract if the task is recurring task or not.
          isRecurring = taskDTO.isRecurring();
          recurringPeriodCronExpression = taskDTO.getRecurringPeriodCronExpression();
          validationService
              .validateTaskIsrecurringAndTaskRecurringPeriodCronExpressionAndSetForAdmin(
                  isRecurring, recurringPeriodCronExpression, task);
          // REQUIRED field for task: 10. extract corporation from provided corporationId.
          corporationId = taskDTO.getCorporationId();
          corporation =
              validationService.validateTaskCorporationIdAndSetForAdmin(
                  corporationId, currentUser, task);
          // REQUIRED field for task: 11. extract assigned staff list for this task from provided
          // assigned staff id list.
          assignedStaffIdSet = taskDTO.getAssignedStaffIdSet();
          validationService.validateTaskAssignedStaffIdSetAndSetForAdmin(
              assignedStaffIdSet, corporation, currentUser, task);
          // REQUIRED field for task: 12. extract assigned manager list for this task from provided
          // manager id list.
          managerIdSet = taskDTO.getManagerIdSet();
          validationService.validateTaskManagerIdSetAndSetForAdmin(
              managerIdSet, corporation, currentUser, task);
          break;
        case "ROLE_USER_STAFF":
          validationService.validateTaskDTOAndSetForAdmin(taskDTO, task);
          // REQUIRED field for task: 1. extract task name
          name = taskDTO.getName();
          validationService.validateTaskNameForAdmin(name, task);
          // REQUIRED field for task: 2. extract task status
          taskStatusString = taskDTO.getTaskStatusString();
          validationService.validateTaskStatusStringAndSetForAdmin(taskStatusString, task);
          // OPTIONAL field for task: 3 extract task priority
          taskPriorityString = taskDTO.getTaskPriorityString();
          validationService.validateTaskPriorityStringAndSetForAdmin(taskPriorityString, task);
          // OPTIONAL field for task: 4. extract task descriptions
          description = taskDTO.getDescription();
          validationService.validateTaskDescriptionAndSetForAdmin(description, task);
          // OPTIONAL field for task: 5. extract task note
          note = taskDTO.getNote();
          validationService.validateTaskNoteAndSetForAdmin(note, task);
          // OPTIONAL field for task: 6. extract task feedback
          feedback = taskDTO.getFeedback();
          validationService.validateTaskFeedbackAndSetForAdmin(feedback, task);
          // REQUIRED field for task: 7. extract task input time (when the task is created by the
          // user.)
          timeInput = taskDTO.getTimeInput();
          validationService.validateTaskTimeInputAndSetForAdmin(timeInput, task);
          // REQUIRED field for task: 8. extract task estimated finish time and calculate task
          // estimated durations.
          timeEstimatedFinish = taskDTO.getTimeEstimatedFinish();
          validationService.validateTaskTimeEstimatedFinishAndTaskTimeInputAndSetForAdmin(
              timeEstimatedFinish, timeInput, task);
          // OPTIONAL field for task: 9. extract if the task is recurring task or not.
          isRecurring = taskDTO.isRecurring();
          recurringPeriodCronExpression = taskDTO.getRecurringPeriodCronExpression();
          validationService
              .validateTaskIsrecurringAndTaskRecurringPeriodCronExpressionAndSetForAdmin(
                  isRecurring, recurringPeriodCronExpression, task);
          // REQUIRED field for task: 10. extract corporation from provided corporationId.
          corporationId = taskDTO.getCorporationId();
          corporation =
              validationService.validateTaskCorporationIdAndSetForAdmin(
                  corporationId, currentUser, task);
          // REQUIRED field for task: 11. extract assigned staff list for this task from provided
          // assigned staff id list.
          assignedStaffIdSet = taskDTO.getAssignedStaffIdSet();
          validationService.validateTaskAssignedStaffIdSetAndSetForStaff(
              assignedStaffIdSet, corporation, currentUser, task);
          // REQUIRED field for task: 12. extract assigned manager list for this task from provided
          // manager id list.
          managerIdSet = taskDTO.getManagerIdSet();
          validationService.validateTaskManagerIdSetAndSetForAdmin(
              managerIdSet, corporation, currentUser, task);
          break;
        default:
          throw ExceptionFactory.create(
              ExceptionType.IllegalRequestBodyFieldsException,
              String.format("Detected invalid role for user: %s", role.getName()));
      }
    }
    return task;
  }

  private TaskDTO mapTaskToTaskDTO(Task task, TaskDTO taskDTO) {
    if (task == null) {
      throw ExceptionFactory.create(
          ExceptionType.IllegalArgumentException,
          "Mapping Task to TaskDTO failed: task cannot be null.");
    }
    if (taskDTO == null) {
      taskDTO = new TaskDTO();
    }
    // Don't need to check task's fields.
    taskDTO.setTaskId(task.getId());
    taskDTO.setName(task.getName());
    taskDTO.setTaskStatusString(task.getTaskStatus().name());
    taskDTO.setTaskPriorityString(task.getTaskPriority().name());
    taskDTO.setDescription(task.getDescription());
    taskDTO.setNote(task.getNote());
    taskDTO.setFeedback(task.getFeedback());
    taskDTO.setTimeInput(task.getTimeInput());
    taskDTO.setTimeEstimatedFinish(task.getTimeEstimatedFinish());
    taskDTO.setRecurring(task.isRecurring());
    taskDTO.setRecurringPeriodCronExpression(task.getRecurringPeriodCronExpression());
    taskDTO.setCorporationId(task.getCorporation().getId());

    Set<Long> assignedStaffIdSet = new HashSet<>();
    Set<UserDTO> assignedStaffUserDTOSet = new HashSet<>();
    for (User staff : task.getStaffSet()) {
      assignedStaffIdSet.add(staff.getId());
      UserDTO assignedStaffUserDTO = new UserDTO();
      assignedStaffUserDTOSet.add(assignedStaffUserDTO);
      assignedStaffUserDTO.setUserId(staff.getId());
      assignedStaffUserDTO.setCorporationId(staff.getCorporation().getId());
      assignedStaffUserDTO.setFullName(staff.getFullName());
      assignedStaffUserDTO.setUsername(staff.getUsername());
      assignedStaffUserDTO.setEmailAddress(staff.getEmailAddress());
      assignedStaffUserDTO.setEnabled(staff.isEnabled());
      assignedStaffUserDTO.setDeleted(staff.isDeleted());
    }
    taskDTO.setAssignedStaffIdSet(assignedStaffIdSet);
    taskDTO.setAssignedStaffUserDTOSet(assignedStaffUserDTOSet);

    Set<Long> managerIdSet = new HashSet<>();
    Set<UserDTO> managerUserDTOSet = new HashSet<>();
    for (User manager : task.getManagerSet()) {
      managerIdSet.add(manager.getId());
      UserDTO managerUserDTO = new UserDTO();
      managerUserDTOSet.add(managerUserDTO);
      managerUserDTO.setUserId(manager.getId());
      managerUserDTO.setCorporationId(manager.getCorporation().getId());
      managerUserDTO.setFullName(manager.getFullName());
      managerUserDTO.setUsername(manager.getUsername());
      managerUserDTO.setEmailAddress(manager.getEmailAddress());
      managerUserDTO.setEnabled(manager.isEnabled());
      managerUserDTO.setDeleted(manager.isDeleted());
    }
    taskDTO.setManagerIdSet(managerIdSet);
    taskDTO.setManagerUserDTOSet(managerUserDTOSet);
    return taskDTO;
  }

  private List<TaskDTO> mapTaskListToTaskDTOList(List<Task> taskList, List<TaskDTO> taskDTOList) {
    if (taskList == null || taskList.isEmpty()) {
      throw ExceptionFactory.create(
          ExceptionType.IllegalArgumentException,
          "Mapping Task List to TaskDTO List failed: task list cannot be empty.");
    }
    if (taskDTOList == null) {
      taskDTOList = new ArrayList<>();
    }
    for (Task task : taskList) {
      TaskDTO taskDTO = new TaskDTO();
      taskDTO = mapTaskToTaskDTO(task, taskDTO);
      taskDTOList.add(taskDTO);
    }
    return taskDTOList;
  }

  private TaskTemplate mapTaskToTaskTemplate(Task task, TaskTemplate taskTemplate) {
    if (task == null) {
      throw ExceptionFactory.create(
          ExceptionType.IllegalArgumentException,
          "Mapping Task to TaskTemplate failed: task cannot be null.");
    }
    if (taskTemplate == null) {
      taskTemplate = new TaskTemplate();
    }
    taskTemplate.setName(task.getName());
    taskTemplate.setDescription(task.getDescription());
    taskTemplate.setNote(task.getNote());
    taskTemplate.setTaskPriority(task.getTaskPriority());
    taskTemplate.setEstimatedDuration(
        task.getTimeEstimatedFinish().getTime() - task.getTimeInput().getTime());
    taskTemplate.setRecurring(task.isRecurring());
    taskTemplate.setRecurringPeriodCronExpression(task.getRecurringPeriodCronExpression());
    taskTemplate.setCorporation(task.getCorporation());
    taskTemplate.setStaffSet(task.getStaffSet());
    taskTemplate.setManagerSet(task.getManagerSet());
    return taskTemplate;
  }

  private TaskTemplateDTO mapTaskTemplateToTaskTemplateDTO(
      TaskTemplate taskTemplate, TaskTemplateDTO taskTemplateDTO) {
    if (taskTemplate == null) {
      throw ExceptionFactory.create(
          ExceptionType.IllegalArgumentException,
          "Mapping TaskTemplate to TaskTemplateDTO failed: taskTemplate cannot be null.");
    }
    if (taskTemplateDTO == null) {
      taskTemplateDTO = new TaskTemplateDTO();
    }
    taskTemplateDTO.setTaskTemplateId(taskTemplate.getId());
    taskTemplateDTO.setName(taskTemplate.getName());
    taskTemplateDTO.setDescription(taskTemplate.getDescription());
    taskTemplateDTO.setNote(taskTemplate.getNote());
    taskTemplateDTO.setTaskPriorityString(taskTemplate.getTaskPriority().name());
    taskTemplateDTO.setEstimatedDuration(taskTemplate.getEstimatedDuration());
    taskTemplateDTO.setRecurring(taskTemplate.isRecurring());
    taskTemplateDTO.setRecurringPeriodCronExpression(
        taskTemplate.getRecurringPeriodCronExpression());
    taskTemplateDTO.setCorporationId(taskTemplate.getCorporation().getId());

    Set<Long> assignedStaffIdSet = new HashSet<>();
    Set<UserDTO> assignedStaffUserDTOSet = new HashSet<>();
    for (User staff : taskTemplate.getStaffSet()) {
      assignedStaffIdSet.add(staff.getId());
      UserDTO assignedStaffUserDTO = new UserDTO();
      assignedStaffUserDTOSet.add(assignedStaffUserDTO);
      assignedStaffUserDTO.setUserId(staff.getId());
      assignedStaffUserDTO.setCorporationId(staff.getCorporation().getId());
      assignedStaffUserDTO.setFullName(staff.getFullName());
      assignedStaffUserDTO.setUsername(staff.getUsername());
      assignedStaffUserDTO.setEmailAddress(staff.getEmailAddress());
      assignedStaffUserDTO.setEnabled(staff.isEnabled());
      assignedStaffUserDTO.setDeleted(staff.isDeleted());
    }
    taskTemplateDTO.setAssignedStaffIdSet(assignedStaffIdSet);
    taskTemplateDTO.setAssignedStaffUserDTOSet(assignedStaffUserDTOSet);

    Set<Long> managerIdSet = new HashSet<>();
    Set<UserDTO> managerUserDTOSet = new HashSet<>();
    for (User manager : taskTemplate.getManagerSet()) {
      managerIdSet.add(manager.getId());
      UserDTO managerUserDTO = new UserDTO();
      managerUserDTOSet.add(managerUserDTO);
      managerUserDTO.setUserId(manager.getId());
      managerUserDTO.setCorporationId(manager.getCorporation().getId());
      managerUserDTO.setFullName(manager.getFullName());
      managerUserDTO.setUsername(manager.getUsername());
      managerUserDTO.setEmailAddress(manager.getEmailAddress());
      managerUserDTO.setEnabled(manager.isEnabled());
      managerUserDTO.setDeleted(manager.isDeleted());
    }
    taskTemplateDTO.setManagerIdSet(managerIdSet);
    taskTemplateDTO.setManagerUserDTOSet(managerUserDTOSet);
    return taskTemplateDTO;
  }

  private List<TaskTemplateDTO> mapTaskTemplateListToTaskTemplateDTOList(
      List<TaskTemplate> taskTemplateList, List<TaskTemplateDTO> taskTemplateDTOList) {
    if (taskTemplateList == null || taskTemplateList.isEmpty()) {
      throw ExceptionFactory.create(
          ExceptionType.IllegalArgumentException,
          "Mapping TaskTemplate List to TaskTemplateDTO List failed: task template list cannot be empty.");
    }
    if (taskTemplateDTOList == null) {
      taskTemplateDTOList = new ArrayList<>();
    }
    for (TaskTemplate taskTemplate : taskTemplateList) {
      TaskTemplateDTO taskTemplateDTO = new TaskTemplateDTO();
      taskTemplateDTO = mapTaskTemplateToTaskTemplateDTO(taskTemplate, taskTemplateDTO);
      taskTemplateDTOList.add(taskTemplateDTO);
    }
    return taskTemplateDTOList;
  }

  private List<Task> getTaskIdListForStaff(User staff) {
    List<TaskStaffUserMapping> taskStaffUserMappingRepositoryList =
        taskStaffUserMappingRepository.findAllByStaff(staff);
    List<Long> taskIdList = new ArrayList<>();
    for (TaskStaffUserMapping taskStaffUserMapping : taskStaffUserMappingRepositoryList) {
      taskIdList.add(taskStaffUserMapping.getTask().getId());
    }
    return getTaskList(taskIdList);
  }

  private List<Task> getTaskIdListForManager(User manager) {
    List<TaskManagerUserMapping> taskManagerUserMappingRepositoryList =
        taskManagerUserMappingRepository.findAllByManager(manager);
    List<Long> taskIdList = new ArrayList<>();
    for (TaskManagerUserMapping taskManagerUserMapping : taskManagerUserMappingRepositoryList) {
      taskIdList.add(taskManagerUserMapping.getTask().getId());
    }
    return getTaskList(taskIdList);
  }

  private List<Task> getTaskIdListForAdmin(User admin) {
    return taskRepository.findAll();
  }
}
