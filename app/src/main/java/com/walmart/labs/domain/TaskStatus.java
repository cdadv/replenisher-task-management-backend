package com.walmart.labs.domain;

public enum TaskStatus {
  /**
   * PENDING means the task was just created. It is waiting to be assigned by manager or waiting to
   * set a manager as reporter.
   */
  CREATED,
  /** ASSIGNED means the task has been assigned to staff or has set up a manager */
  ASSIGNED,
  /** IN_PROGRESS means the task has been assigned by either staff(s) themselves or by manager */
  IN_PROGRESS,
  /** FINISHED means the task has been finished. */
  FINISHED;

  /**
   * lookup function will find the related enum element by input task status string (converted to
   * uppercase) and return with the enum when the input string equals the name of enum element.
   * Otherwise, return null.
   *
   * @param inputTaskStatusString input TaskStatus string is passed from client (frontend or other
   *     backend) as String
   * @return TaskStatus enum when exist and null when doesn't exist
   */
  public static TaskStatus lookup(String inputTaskStatusString) {
    for (TaskStatus status : values()) {
      if (status.name().equals(inputTaskStatusString.toUpperCase())) {
        return status;
      }
    }
    return null;
  }
}
