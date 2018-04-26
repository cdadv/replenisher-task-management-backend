package com.walmart.labs.service;

import com.walmart.labs.domain.Corporation;
import com.walmart.labs.domain.Task;
import com.walmart.labs.domain.TaskStatus;
import com.walmart.labs.domain.User;
import com.walmart.labs.dto.TaskDTO;
import com.walmart.labs.exception.ExceptionFactory;
import com.walmart.labs.exception.ExceptionType;
import com.walmart.labs.repository.CorporationRepository;
import com.walmart.labs.repository.TaskRepository;
import com.walmart.labs.repository.UserRepository;
import com.walmart.labs.repository.UserRoleRepository;
import com.walmart.labs.util.UtilService;
import com.walmart.labs.util.ValidationService;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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

  /**
   * Extract all the information from TaskDTO to Task and then save.
   *
   * @param taskDTO TaskDTO passed from frontend or client.
   */
  public void createTask(TaskDTO taskDTO) {
    Task task = new Task();

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
        task.setEstimatedDuration(estimatedDuration);
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
    List<Long> assignedStaffIdList = taskDTO.getAssignedStaffIdList();
    List<User> assignedStaffList = null;
    if (assignedStaffIdList != null && !assignedStaffIdList.isEmpty()) {
      // TODO: check if provided staff id refer to a staff
      assignedStaffList =
          userRepository.findAllByIdInAndCorporation(assignedStaffIdList, corporation);
      if (assignedStaffList != null && !assignedStaffList.isEmpty()) {
        task.setStaffList(assignedStaffList);
      } else {
        throw ExceptionFactory.create(
            ExceptionType.IllegalRequestBodyFieldsException,
            String.format(
                "Detected invalid staff list within creating task request: cannot find corresponding records in database with provided staff ids %s",
                utilService.listOfLongTypeToCommaSeparatedString(assignedStaffIdList)));
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
    List<Long> managerIdList = taskDTO.getManagerIdList();
    List<User> managerList = null;
    if (managerIdList != null && !managerIdList.isEmpty()) {
      // TODO: check if provided manager id refer to a manager
      managerList = userRepository.findAllByIdInAndCorporation(managerIdList, corporation);
      if (managerList != null && !managerList.isEmpty()) {
        task.setManagerList(managerList);
      } else {
        throw ExceptionFactory.create(
            ExceptionType.IllegalRequestBodyFieldsException,
            String.format(
                "Detected invalid manager list within creating task request: cannot find corresponding records in database with provided manager ids %s",
                utilService.listOfLongTypeToCommaSeparatedString(managerIdList)));
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
          taskRepository.findByNameAndCorporationAndStaffList(name, corporation, assignedStaffList);
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

    createTask(task);
    logger.info("Created a task successfully!");
  }

  public void createTask(Task task) {
    taskRepository.save(task);
  }
}
