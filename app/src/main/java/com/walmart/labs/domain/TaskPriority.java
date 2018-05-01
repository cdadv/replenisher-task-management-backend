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

  public static int compareTwoPriorities(String p1String, String p2String) {
    if (p1String.equals(p2String)) {
      return 0;
    } else {
      TaskPriority p1 = TaskPriority.valueOf(p1String);
      TaskPriority p2 = TaskPriority.valueOf(p2String);
      if (p1.equals(TaskPriority.HIGH)) {
        return -1;
      } else if (p1.equals(TaskPriority.MEDIUM) && p2.equals(TaskPriority.LOW)) {
        return -1;
      } else if (p1.equals(TaskPriority.MEDIUM) && p2.equals(TaskPriority.HIGH)) {
        return 1;
      } else {
        return 1;
      }
    }
  }
}
