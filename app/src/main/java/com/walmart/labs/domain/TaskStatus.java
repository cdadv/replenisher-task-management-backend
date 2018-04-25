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
  FINISHED
}
