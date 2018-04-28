package com.walmart.labs.util;

import com.walmart.labs.domain.Task;
import com.walmart.labs.domain.User;
import com.walmart.labs.domain.UserRole;
import com.walmart.labs.exception.ExceptionFactory;
import com.walmart.labs.exception.ExceptionType;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ValidationService {
  public boolean validateTextField(String text) {
    // 65535 is the maximum length defined for domain property 'description', 'note', 'feedback' */
    return text.length() <= 65535;
  }

  /**
   * This method is for verifying to see if the user has the authority to create, update or delete a task.
   *
   * @param user
   * @param task
   */
  public void validateStaffListAndManagerListInTask(User user, Task task) {
    Set<User> taskAssignedStaffSet = task.getStaffSet();
    Set<User> taskManagerSet = task.getManagerSet();
    Set<UserRole> allowedRoleSet = user.getAllowedRoleSet();
    if (!user.getCorporation().equals(task.getCorporation())) {
      throw ExceptionFactory.create(
          ExceptionType.IllegalRequestBodyFieldsException,
          "Detected invalid current user: current user belongs to different corporation with task's corporation.");
    }
    boolean assignedStaffContainsLoggedInUser = false;
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
                    "Detected invalid current user: staffs can only assign one manager for themselves");
          }
          for (User assignedStaff: taskAssignedStaffSet) {
            if(assignedStaff.equals(user)) {
              assignedStaffContainsLoggedInUser = true;
            }
          }
          if (!assignedStaffContainsLoggedInUser) {
            throw ExceptionFactory.create(
                    ExceptionType.IllegalRequestParametersException,
                    "Detected invalid staff list within creating/updating/deleting task request: assigned staff id list must contain the request staff user self.");
          }
          break;
        default:
          throw ExceptionFactory.create(
                  ExceptionType.IllegalRequestParametersException,
                  String.format("Detected invalid role for user: %s", role.getName()));
      }
    }
  }
}
