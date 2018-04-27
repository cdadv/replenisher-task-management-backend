package com.walmart.labs.domain;

public enum TaskPriority {
  LOW,
  MEDIUM,
  HIGH;

  /**
   * lookup function will find the related enum element by input task priority string (converted to
   * uppercase) and return with the enum when the input string equals the name of enum element.
   * Otherwise, return null.
   *
   * @param inputTaskPriorityString input TaskStatus string is passed from client (frontend or other
   *     backend) as String
   * @return TaskStatus enum when exist and null when doesn't exist
   */
  public static TaskPriority lookup(String inputTaskPriorityString) {
    for (TaskPriority priority : values()) {
      if (priority.name().equals(inputTaskPriorityString.toUpperCase())) {
        return priority;
      }
    }
    return null;
  }
}
