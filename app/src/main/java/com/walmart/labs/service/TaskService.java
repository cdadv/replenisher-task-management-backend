package com.walmart.labs.service;

import com.walmart.labs.domain.Corporation;
import com.walmart.labs.domain.Task;
import com.walmart.labs.domain.TaskStatus;
import com.walmart.labs.domain.User;
import com.walmart.labs.domain.UserRole;
import com.walmart.labs.dto.TaskDTO;
import com.walmart.labs.repository.CorporationRepository;
import com.walmart.labs.repository.TaskRepository;
import com.walmart.labs.repository.UserRepository;
import com.walmart.labs.repository.UserRoleRepository;
import com.walmart.labs.util.ValidationService;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import javax.swing.text.html.Option;
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
  @Autowired private CorporationRepository corporationRepository;
  @Autowired private UserRepository userRepository;
  @Autowired private UserRoleRepository userRoleRepository;

  public void createTask(TaskDTO taskDTO) {
    Task task = new Task();

    String name = taskDTO.getName();
    if (name != null) {
      task.setName(name);
    } else {
      // TODO: error handling
      throw new RuntimeException();
    }

    String taskStatusString = taskDTO.getTaskStatusString();
    TaskStatus taskStatus = TaskStatus.lookup(taskStatusString);
    if (taskStatus != null) {
      task.setTaskStatus(taskStatus);
    } else {
      // TODO: error handling
      throw new RuntimeException();
    }

    String description = taskDTO.getDescription();
    if (description == null || validationService.validateTextField(description)) {
      task.setDescription(description);
    } else {
      // TODO: error handling
      throw new RuntimeException();
    }

    String note = taskDTO.getNote();
    if (note == null || validationService.validateTextField(note)) {
      task.setNote(note);
    } else {
      // TODO: error handling
      throw new RuntimeException();
    }

    String feedback = taskDTO.getFeedback();
    if (feedback == null || validationService.validateTextField(feedback)) {
      task.setFeedback(feedback);
    } else {
      // TODO: error handling
      throw new RuntimeException();
    }

    Date timeInput = taskDTO.getTimeInput();
    if (timeInput != null) {
      task.setTimeInput(timeInput);
    } else {
      // TODO: error handling
      throw new RuntimeException();
    }

    Date timeEstimatedFinish = taskDTO.getTimeEstimatedFinish();
    if (timeEstimatedFinish != null) {
      long estimatedDuration = timeEstimatedFinish.getTime() - timeInput.getTime();
      if (estimatedDuration > 0) {
        task.setEstimatedDuration(estimatedDuration);
      } else {
        // TODO: error handling: timeInput greater than timeEstimatedFinish
        throw new RuntimeException();
      }
    } else {
      // TODO: error handling
      throw new RuntimeException();
    }

    boolean isRecurring = taskDTO.isRecurring();
    if (isRecurring) {
      task.setRecurring(true);
      String recurringPeriodCronExpression = taskDTO.getRecurringPeriodCronExpression();
      // TODO: validate recurringPeriodCronExpression
      task.setRecurringPeriodCronExpression(recurringPeriodCronExpression);
    } else {
      task.setRecurring(false);
    }

    Corporation corporation = null;
    Long corporationId = taskDTO.getCorporationId();
    if (corporationId != null) {

      Optional<Corporation> optionalCorporation = corporationRepository.findById(corporationId);
      if (optionalCorporation.isPresent()) {
        corporation = optionalCorporation.get();
        task.setCorporation(corporation);
      } else {
        // TODO: error handling
        throw new RuntimeException();
      }
    } else {
      // TODO: error handling
      throw new RuntimeException();
    }

    List<Long> assignedStaffIdList = taskDTO.getAssignedStaffIdList();
    if (assignedStaffIdList != null && !assignedStaffIdList.isEmpty()) {
      // TODO: check if provided staff id refer to a staff
      List<User> assignedStaffList =
          userRepository.findAllByIdInAndCorporation(
              assignedStaffIdList, corporation);
      if (assignedStaffList != null && !assignedStaffList.isEmpty()) {
        task.setStaffList(assignedStaffList);
      } else {
        // TODO: error handling
        throw new RuntimeException();
      }
    } else {
      // TODO: error handling
      throw new RuntimeException();
    }

    List<Long> managerIdList = taskDTO.getManagerIdList();
    if (managerIdList != null && !managerIdList.isEmpty()) {
      // TODO: check if provided manager id refer to a manager
      List<User> managerList =
          userRepository.findAllByIdInAndCorporation(
              managerIdList, corporation);
      if (managerList != null && !managerList.isEmpty()) {
        task.setManagerList(managerList);
      } else {
        // TODO: error handling
        throw new RuntimeException();
      }
    } else {
      // TODO: error handling
      throw new RuntimeException();
    }
    createTask(task);
    logger.info("Created a task successfully!");
  }

  public void createTask(Task task) {
    taskRepository.save(task);
  }
}
