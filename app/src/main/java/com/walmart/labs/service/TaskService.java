package com.walmart.labs.service;

import com.walmart.labs.domain.Corporation;
import com.walmart.labs.domain.Task;
import com.walmart.labs.domain.TaskPriority;
import com.walmart.labs.domain.TaskStatus;
import com.walmart.labs.domain.User;
import com.walmart.labs.domain.UserRole;
import com.walmart.labs.domain.mapping.TaskManagerUserMapping;
import com.walmart.labs.domain.mapping.TaskStaffUserMapping;
import com.walmart.labs.dto.TaskDTO;
import com.walmart.labs.exception.ExceptionFactory;
import com.walmart.labs.exception.ExceptionType;
import com.walmart.labs.repository.CorporationRepository;
import com.walmart.labs.repository.TaskRepository;
import com.walmart.labs.repository.UserRepository;
import com.walmart.labs.repository.UserRoleRepository;
import com.walmart.labs.repository.mapping.TaskManagerUserMappingRepository;
import com.walmart.labs.repository.mapping.TaskStaffUserMappingRepository;
import com.walmart.labs.util.UtilService;
import com.walmart.labs.util.ValidationService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
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
  @Autowired private ValidationService validationService;
  @Autowired private UtilService utilService;
  @Autowired private CorporationRepository corporationRepository;
  @Autowired private UserRepository userRepository;
  @Autowired private UserRoleRepository userRoleRepository;
  @Autowired private TaskStaffUserMappingRepository taskStaffUserMappingRepository;
  @Autowired private TaskManagerUserMappingRepository taskManagerUserMappingRepository;

  /**
   * Query all allowed task based on user's role. And extract all the information from Task to
   * TaskDTO.
   *
   * @param user user is used for determining which tasks to return.
   * @return list of TaskDTO
   */
  public List<TaskDTO> getTaskList(User user) {
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
    taskDTOList = mapTaskListToTaskDTOList(taskList, taskDTOList);
    logger.info("Queried a task successfully!");
    return taskDTOList;
  }

  public List<Task> getTaskList(List<Long> taskIdList) {
    return taskRepository.findAllById(taskIdList);
  }

  /**
   * Extract all the information from TaskDTO to Task and then save.
   *
   * @param taskDTO TaskDTO passed from frontend or client.
   */
  public void createTask(User user, TaskDTO taskDTO) {
    Task task = new Task();
    task = mapTaskDTOToTask(taskDTO, task);
    Set<User> taskAssignedStaffSet = task.getStaffSet();
    Set<User> taskManagerSet = task.getManagerSet();
    Set<UserRole> allowedRoleSet = user.getAllowedRoleSet();
    for (UserRole role : allowedRoleSet) {
      switch (role.getName()) {
          // Assuming there are only three roles supported in backend.
          // TODO: add more sophisticated checking between logged in user and taskAssignedStaffSet
          // and taskManagerSet.
        case "ROLE_ADMIN":
          break;
        case "ROLE_USER_MANAGER":
          break;
        case "ROLE_USER_STAFF":
          if (taskManagerSet.size() > 1) {
            throw ExceptionFactory.create(
                ExceptionType.IllegalRequestParametersException,
                "Detected invalid user usage: staffs can only assign one manager for themselves");
          }
          break;
        default:
          throw ExceptionFactory.create(
              ExceptionType.IllegalRequestParametersException,
              String.format("Detected invalid role for user: %s", role.getName()));
      }
    }
    createTask(task);
    logger.info("Created a task successfully!");
  }

  public void createTask(Task task) {
    taskRepository.save(task);
  }

  private Task mapTaskDTOToTask(TaskDTO taskDTO, Task task) {
    if (taskDTO == null) {
      throw ExceptionFactory.create(
          ExceptionType.IllegalRequestBodyFieldsException,
          "Detected invalid TaskDTO within creating task request: taskDTO cannot be empty.");
    }
    if (task == null) {
      task = new Task();
    }
    /*
     REQUIRED field for task

     2. extract task status
    */
    String taskStatusString = taskDTO.getTaskStatusString();
    TaskStatus taskStatus = TaskStatus.lookup(taskStatusString);
    if (taskStatus != null) {
      task.setTaskStatus(taskStatus);
    } else {
      throw ExceptionFactory.create(
          ExceptionType.IllegalRequestBodyFieldsException,
          String.format(
              "Detected invalid task status within creating task request: task status should be one of the following: %s",
              Arrays.toString(utilService.getEnumNameList(TaskStatus.class))));
    }

    /*
    OPTIONAL field for task

    2.5 extract task priority
     */
    String taskPriorityString = taskDTO.getTaskPriorityString();
    TaskPriority taskPriority = TaskPriority.LOW;
    if (taskPriorityString == null) {
      task.setTaskPriority(taskPriority);
    } else {
      taskPriority = TaskPriority.lookup(taskPriorityString);
      if (taskPriority != null) {
        task.setTaskPriority(taskPriority);
      } else {
        throw ExceptionFactory.create(
            ExceptionType.IllegalRequestBodyFieldsException,
            String.format(
                "Detected invalid task priority within creating task request: task priority should be one of the following: %s",
                Arrays.toString(utilService.getEnumNameList(TaskPriority.class))));
      }
    }

    /*
     OPTIONAL field for task

     3. extract task descriptions
    */
    String description = taskDTO.getDescription();
    if (description == null || validationService.validateTextField(description)) {
      task.setDescription(description);
    } else {
      throw ExceptionFactory.create(
          ExceptionType.IllegalRequestBodyFieldsException,
          "Detected invalid task description within creating task request: text for description cannot contains more than 65535 characters.");
    }

    /*
     OPTIONAL field for task

     4. extract task note
    */
    String note = taskDTO.getNote();
    if (note == null || validationService.validateTextField(note)) {
      task.setNote(note);
    } else {
      throw ExceptionFactory.create(
          ExceptionType.IllegalRequestBodyFieldsException,
          "Detected invalid task description within creating task request: text for note cannot contains more than 65535 characters.");
    }

    /*
     OPTIONAL field for task

     5. extract task feedback
    */
    String feedback = taskDTO.getFeedback();
    if (feedback == null || validationService.validateTextField(feedback)) {
      task.setFeedback(feedback);
    } else {
      throw ExceptionFactory.create(
          ExceptionType.IllegalRequestBodyFieldsException,
          "Detected invalid task description within creating task request: text for feedback cannot contains more than 65535 characters.");
    }

    /*
     REQUIRED field for task

     6. extract task input time (when the task is created by the user.)
    */
    Date timeInput = taskDTO.getTimeInput();
    if (timeInput != null) {
      task.setTimeInput(timeInput);
    } else {
      throw ExceptionFactory.create(
          ExceptionType.IllegalRequestBodyFieldsException,
          "Detected invalid input time within creating task request: input time cannot be empty.");
    }

    /*
     REQUIRED field for task

     7. extract task estimated finish time and calculate task estimated durations.
    */
    Date timeEstimatedFinish = taskDTO.getTimeEstimatedFinish();
    if (timeEstimatedFinish != null) {
      long estimatedDuration = timeEstimatedFinish.getTime() - timeInput.getTime();
      if (estimatedDuration > 0) {
        task.setTimeEstimatedFinish(timeEstimatedFinish);
      } else {
        throw ExceptionFactory.create(
            ExceptionType.IllegalRequestBodyFieldsException,
            "Detected invalid input time or estimated finish time within creating task request: input time cannot be greater than estimated finish time.");
      }
    } else {
      throw ExceptionFactory.create(
          ExceptionType.IllegalRequestBodyFieldsException,
          "Detected invalid estimated finish time within creating task request: estimated finish time cannot be empty.");
    }

    /*
     OPTIONAL field for task

     8. extract if the task is recurring task or not.
    */
    boolean isRecurring = taskDTO.isRecurring();
    if (isRecurring) {
      task.setRecurring(true);
      /*
       OPTIONAL field for task

       9. if the task is a recurring task, this field will be used for storing the period of the recurring.
      */
      String recurringPeriodCronExpression = taskDTO.getRecurringPeriodCronExpression();
      // TODO: validate recurringPeriodCronExpression
      task.setRecurringPeriodCronExpression(recurringPeriodCronExpression);
    } else {
      task.setRecurring(false);
    }

    /*
     REQUIRED field for task

     10. extract corporation from provided corporationId.
    */
    Corporation corporation = null;
    Long corporationId = taskDTO.getCorporationId();
    if (corporationId != null) {
      Optional<Corporation> optionalCorporation = corporationRepository.findById(corporationId);
      if (optionalCorporation.isPresent()) {
        corporation = optionalCorporation.get();
        task.setCorporation(corporation);
      } else {
        throw ExceptionFactory.create(
            ExceptionType.IllegalRequestBodyFieldsException,
            String.format(
                "Detected invalid corporation id within creating task request: cannot find corresponding records in database with corporation id %s",
                Long.toString(corporationId)));
      }
    } else {
      throw ExceptionFactory.create(
          ExceptionType.IllegalRequestBodyFieldsException,
          "Detected invalid corporation id within creating task request: corporation id cannot be empty.");
    }

    /*
     REQUIRED field for task

     11. extract assigned staff list for this task from provided assigned staff id list.
    */
    Set<Long> assignedStaffIdSet = taskDTO.getAssignedStaffIdSet();
    Set<User> assignedStaffSet = null;
    if (assignedStaffIdSet != null && !assignedStaffIdSet.isEmpty()) {
      // TODO: check if provided staff id refer to a staff
      assignedStaffSet =
          userRepository.findAllByIdInAndCorporation(assignedStaffIdSet, corporation);
      if (assignedStaffSet != null && !assignedStaffSet.isEmpty()) {
        task.setStaffSet(assignedStaffSet);
      } else {
        throw ExceptionFactory.create(
            ExceptionType.IllegalRequestBodyFieldsException,
            String.format(
                "Detected invalid staff list within creating task request: cannot find corresponding records in database with provided staff ids %s",
                utilService.setOfLongTypeToCommaSeparatedString(assignedStaffIdSet)));
      }
    } else {
      throw ExceptionFactory.create(
          ExceptionType.IllegalRequestBodyFieldsException,
          "Detected invalid staff list within creating task request: assigned staff list cannot be empty.");
    }

    /*
     REQUIRED field for task

     12. extract assigned manager list for this task from provided manager id list.
    */
    Set<Long> managerIdSet = taskDTO.getManagerIdSet();
    Set<User> managerSet = null;
    if (managerIdSet != null && !managerIdSet.isEmpty()) {
      // TODO: check if provided manager id refer to a manager
      managerSet = userRepository.findAllByIdInAndCorporation(managerIdSet, corporation);
      if (managerSet != null && !managerSet.isEmpty()) {
        task.setManagerSet(managerSet);
      } else {
        throw ExceptionFactory.create(
            ExceptionType.IllegalRequestBodyFieldsException,
            String.format(
                "Detected invalid manager list within creating task request: cannot find corresponding records in database with provided manager ids %s",
                utilService.setOfLongTypeToCommaSeparatedString(managerIdSet)));
      }
    } else {
      throw ExceptionFactory.create(
          ExceptionType.IllegalRequestBodyFieldsException,
          "Detected invalid manager list within creating task request: manager list cannot be empty.");
    }

    /*
     REQUIRED field for task

     1. extract task name
    */
    String name = taskDTO.getName();
    if (name != null) {
      Task existingTaskWithSameName =
          taskRepository.findByNameAndCorporationAndStaffSet(name, corporation, assignedStaffSet);
      if (existingTaskWithSameName == null) {
        task.setName(name);
      } else {
        throw ExceptionFactory.create(
            ExceptionType.IllegalRequestBodyFieldsException,
            "Detected invalid task name within creating task request: duplicated task name for same corporation and same staffs.");
      }

    } else {
      throw ExceptionFactory.create(
          ExceptionType.IllegalRequestBodyFieldsException,
          "Detected invalid task name within creating task request: task name cannot be empty.");
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
    taskDTO.setAssignedStaffIdSet(
        new HashSet<>(task.getStaffSet().stream().map(User::getId).collect(Collectors.toList())));
    taskDTO.setManagerIdSet(
        new HashSet<>(task.getManagerSet().stream().map(User::getId).collect(Collectors.toList())));
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
