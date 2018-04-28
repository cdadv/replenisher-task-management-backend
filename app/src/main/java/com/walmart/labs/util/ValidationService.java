package com.walmart.labs.util;

import com.walmart.labs.domain.Corporation;
import com.walmart.labs.domain.Task;
import com.walmart.labs.domain.TaskPriority;
import com.walmart.labs.domain.TaskStatus;
import com.walmart.labs.domain.User;
import com.walmart.labs.domain.UserRole;
import com.walmart.labs.dto.TaskDTO;
import com.walmart.labs.exception.ExceptionFactory;
import com.walmart.labs.exception.ExceptionType;
import com.walmart.labs.repository.CorporationRepository;
import com.walmart.labs.repository.TaskRepository;
import com.walmart.labs.repository.UserRepository;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {
  @Autowired private UtilService utilService;
  @Autowired private CorporationRepository corporationRepository;
  @Autowired private UserRepository userRepository;
  @Autowired private TaskRepository taskRepository;

  public boolean validateTextField(String text) {
    // 65535 is the maximum length defined for domain property 'description', 'note', 'feedback' */
    return text.length() <= 65535;
  }

  public Task validateTaskIdForUpdatingOrDeletingForAdmin(Long taskId, User currentUser) {
    Task task = null;
    Optional<Task> taskOptional = taskRepository.findById(taskId);
    if (taskOptional.isPresent()) {
      task = taskOptional.get();
    } else {
      throw ExceptionFactory.create(
          ExceptionType.IllegalRequestParametersException,
          String.format(
              "Detected invalid task id within updating task request: cannot find corresponding records in database with task id %s",
              Long.toString(taskId)));
    }
    return task;
  }

  public Task validateTaskIdForUpdatingOrDeletingForManager(Long taskId, User currentUser) {
    Task task = null;
    Optional<Task> taskOptional = taskRepository.findById(taskId);
    if (taskOptional.isPresent()) {
      task = taskOptional.get();
      if (!task.getCorporation().getId().equals(currentUser.getCorporation().getId())) {
        throw ExceptionFactory.create(
            ExceptionType.IllegalRequestParametersException,
            "Detected invalid user: manager/staff are not allowed to delete task for other corporation.");
      }
    } else {
      throw ExceptionFactory.create(
          ExceptionType.IllegalRequestParametersException,
          String.format(
              "Detected invalid task id within updating task request: cannot find corresponding records in database with task id %s",
              Long.toString(taskId)));
    }
    return task;
  }

  public void validateTaskDTOAndSetForAdmin(TaskDTO taskDTO, Task task) {
    if (taskDTO == null) {
      throw ExceptionFactory.create(
          ExceptionType.IllegalRequestBodyFieldsException,
          "Detected invalid TaskDTO within creating task request: taskDTO cannot be empty.");
    }
  }

  public void validateTaskNameForAdmin(String name, Task task) {
    if (name != null) {
      task.setName(name);
    } else {
      throw ExceptionFactory.create(
          ExceptionType.IllegalRequestBodyFieldsException,
          "Detected invalid task name within creating task request: task name cannot be empty.");
    }
  }

  public void validateTaskStatusStringAndSetForAdmin(String taskStatusString, Task task) {
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
  }

  public void validateTaskPriorityStringAndSetForAdmin(String taskPriorityString, Task task) {
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
  }

  public void validateTaskDescriptionAndSetForAdmin(String description, Task task) {
    if (description == null || this.validateTextField(description)) {
      task.setDescription(description);
    } else {
      throw ExceptionFactory.create(
          ExceptionType.IllegalRequestBodyFieldsException,
          "Detected invalid task description within creating task request: text for description cannot contains more than 65535 characters.");
    }
  }

  public void validateTaskNoteAndSetForAdmin(String note, Task task) {
    if (note == null || this.validateTextField(note)) {
      task.setNote(note);
    } else {
      throw ExceptionFactory.create(
          ExceptionType.IllegalRequestBodyFieldsException,
          "Detected invalid task description within creating task request: text for note cannot contains more than 65535 characters.");
    }
  }

  public void validateTaskFeedbackAndSetForAdmin(String feedback, Task task) {
    if (feedback == null || this.validateTextField(feedback)) {
      task.setFeedback(feedback);
    } else {
      throw ExceptionFactory.create(
          ExceptionType.IllegalRequestBodyFieldsException,
          "Detected invalid task description within creating task request: text for feedback cannot contains more than 65535 characters.");
    }
  }

  public void validateTaskTimeInputAndSetForAdmin(Date timeInput, Task task) {
    if (timeInput != null) {
      task.setTimeInput(timeInput);
    } else {
      throw ExceptionFactory.create(
          ExceptionType.IllegalRequestBodyFieldsException,
          "Detected invalid input time within creating task request: input time cannot be empty.");
    }
  }

  public void validateTaskTimeEstimatedFinishAndTaskTimeInputAndSetForAdmin(
      Date timeEstimatedFinish, Date timeInput, Task task) {
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
  }

  public void validateTaskIsrecurringAndTaskRecurringPeriodCronExpressionAndSetForAdmin(
      boolean isRecurring, String recurringPeriodCronExpression, Task task) {
    if (isRecurring) {
      task.setRecurring(true);
      /*
       OPTIONAL field for task

       9. if the task is a recurring task, this field will be used for storing the period of the recurring.
      */
      // TODO: validate recurringPeriodCronExpression
      task.setRecurringPeriodCronExpression(recurringPeriodCronExpression);
    } else {
      task.setRecurring(false);
    }
  }

  /*
  1. check when editing a task (corporation is not null), manager/staff should not be allowed to update the old task with a different corporation.
  2. check if corporationId is null
  3. if corporationId is not null, check if there is a corresponding corporation record in database
  4. check if current user belongs to the same corporation provided in dto, manager/staff should not be allowed to update the task with a different corporation
   */
  public Corporation validateTaskCorporationIdAndSetForAdmin(
      Long corporationId, User currentUser, Task task) {
    Corporation corporation = null;
    if (task.getCorporation() != null && !task.getCorporation().getId().equals(corporationId)) {
      throw ExceptionFactory.create(
          ExceptionType.IllegalRequestBodyFieldsException,
          "Detected invalid corporation id within updating task request: task to update must have the same corporation id as old task (change task corporation id is not allowed)");
    }
    if (corporationId != null) {
      Optional<Corporation> optionalCorporation = corporationRepository.findById(corporationId);
      if (optionalCorporation.isPresent()) {
        corporation = optionalCorporation.get();
        if (!corporation.getId().equals(currentUser.getCorporation().getId())) {
          throw ExceptionFactory.create(
              ExceptionType.IllegalRequestBodyFieldsException,
              String.format(
                  "Detected invalid corporation id within creating task request: current user does not have authorization to create a task with corporation id %s",
                  Long.toString(corporationId)));
        }
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
    return corporation;
  }

  /*
  1. check if assignedStaffIdSet is null or empty
  2. if assignedStaffIdSet is not null or not empty, check if there are corresponding staff user records in database. query by assignedStaffIdSet and corporation in dto. Get assignedStaffSet
  3. loop over the assignedStaffSet to check if all the staff in it has 'ROLE_STAFF' role.
  4. loop over the assignedStaffSet to check if all the staff in it has same corporation that are specified in dto
   */
  public void validateTaskAssignedStaffIdSetAndSetForAdmin(
      Set<Long> assignedStaffIdSet, Corporation corporation, User currentUser, Task task) {
    Set<User> assignedStaffSet = null;
    if (assignedStaffIdSet != null && !assignedStaffIdSet.isEmpty()) {
      assignedStaffSet =
          userRepository.findAllByIdInAndCorporation(assignedStaffIdSet, corporation);
      if (assignedStaffSet != null && !assignedStaffSet.isEmpty()) {
        // verify if provided user id is a staff
        // among all the provided staff id in the set, an error will be thrown if there is an id
        // does
        // not have a staff role.
        for (User assignedStaff : assignedStaffSet) {
          Set<UserRole> userRoleSet = assignedStaff.getAllowedRoleSet();
          for (UserRole role : userRoleSet) {
            if (!role.getName().equals("ROLE_USER_STAFF")) {
              throw ExceptionFactory.create(
                  ExceptionType.IllegalRequestBodyFieldsException,
                  String.format(
                      "Detected invalid staff id of staff id list within creating task request: user id %s is not mapped to a staff role",
                      assignedStaff.getId()));
            }
          }
          if (!assignedStaff.getCorporation().getId().equals(corporation.getId())) {
            throw ExceptionFactory.create(
                ExceptionType.IllegalRequestBodyFieldsException,
                "Detected invalid staff of staff list within creating task request: staff cannot assign other corporation's staff to this task");
          }
        }
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
          "Detected invalid staff id list within creating task request: assigned staff id list cannot be empty.");
    }
  }

  /*
  1. check if assignedStaffIdSet is null or empty
  2. if assignedStaffIdSet is not null or not empty, check if there are corresponding staff user records in database. query by assignedStaffIdSet and corporation in dto. Get assignedStaffSet
  3. loop over the assignedStaffSet to check if all the staff in it has 'ROLE_STAFF' role.
  4. loop over the assignedStaffSet to check if all the staff in it has same corporation that are specified in dto
  5. staffs must include themselves in the updated staffSet (in editing or creating)
   */
  public void validateTaskAssignedStaffIdSetAndSetForStaff(
      Set<Long> assignedStaffIdSet, Corporation corporation, User currentUser, Task task) {
    Set<User> assignedStaffSet = null;
    boolean assignedStaffListInOldTaskContainsCurrentUser = false;
    if (task.getStaffSet() == null) {
      assignedStaffListInOldTaskContainsCurrentUser = true;
    }
    if (assignedStaffIdSet != null && !assignedStaffIdSet.isEmpty()) {
      assignedStaffSet =
          userRepository.findAllByIdInAndCorporation(assignedStaffIdSet, corporation);
      if (assignedStaffSet != null && !assignedStaffSet.isEmpty()) {
        // verify if provided user id is a staff
        // among all the provided staff id in the set, an error will be thrown if there is an id
        // does
        // not have a staff role.
        for (User assignedStaff : assignedStaffSet) {
          if (task.getStaffSet() != null && assignedStaff.getId().equals(currentUser.getId())) {
            assignedStaffListInOldTaskContainsCurrentUser = true;
          }
          Set<UserRole> userRoleSet = assignedStaff.getAllowedRoleSet();
          for (UserRole role : userRoleSet) {
            if (!role.getName().equals("ROLE_USER_STAFF")) {
              throw ExceptionFactory.create(
                  ExceptionType.IllegalRequestBodyFieldsException,
                  String.format(
                      "Detected invalid staff id of staff id list within creating task request: user id %s is not mapped to a staff role",
                      assignedStaff.getId()));
            }
          }
          if (!assignedStaff.getCorporation().getId().equals(corporation.getId())) {
            throw ExceptionFactory.create(
                ExceptionType.IllegalRequestBodyFieldsException,
                "Detected invalid staff of staff list within creating task request: staff cannot assign other corporation's staff to this task");
          }
        }
        if (!assignedStaffListInOldTaskContainsCurrentUser) {
          throw ExceptionFactory.create(
              ExceptionType.IllegalRequestBodyFieldsException,
              "Detected invalid current user: staff list can be updated only when the staff list of old task contains current user");
        }
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
          "Detected invalid staff id list within creating task request: assigned staff id list cannot be empty.");
    }
  }

  /*
  1. check if managerIdSet is null or empty
  2. if managerIdSet is not null or not empty, check if there are corresponding staff user records in database. query by assignedStaffIdSet and corporation in dto. Get assignedStaffSet
  3. loop over the managerSet to check if all the manager in it has 'ROLE_USER_MANAGER' role.
  4. loop over the managerSet to check if all the manager in it has same corporation that are specified in dto
  */
  public void validateTaskManagerIdSetAndSetForAdmin(
      Set<Long> managerIdSet, Corporation corporation, User currentUser, Task task) {
    Set<User> managerSet = null;
    if (managerIdSet != null && !managerIdSet.isEmpty()) {
      managerSet = userRepository.findAllByIdInAndCorporation(managerIdSet, corporation);
      if (managerSet != null && !managerSet.isEmpty()) {
        // verify if provided user id is a staff
        // among all the provided staff id in the set, an error will be thrown if there the id does
        // not have a staff role.
        for (User manager : managerSet) {
          Set<UserRole> userRoleSet = manager.getAllowedRoleSet();
          for (UserRole role : userRoleSet) {
            if (!role.getName().equals("ROLE_USER_MANAGER")) {
              throw ExceptionFactory.create(
                  ExceptionType.IllegalRequestBodyFieldsException,
                  String.format(
                      "Detected invalid manager id of manager id list within creating task request: user id %s is not mapped to a manager role",
                      manager.getId()));
            }
          }
          if (!manager.getCorporation().getId().equals(corporation.getId())) {
            throw ExceptionFactory.create(
                ExceptionType.IllegalRequestBodyFieldsException,
                "Detected invalid manager of manager list within creating task request: cannot assign other corporation's manager to this task");
          }
        }
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
  }
}
