package com.walmart.labs.comparator;

import com.walmart.labs.domain.TaskPriority;
import com.walmart.labs.dto.TaskDTO;
import java.util.Comparator;
import java.util.Date;

public class SortByRankComparator implements Comparator<TaskDTO> {

  @Override
  public int compare(TaskDTO o1, TaskDTO o2) {
    int priorityComparisonResult =
        TaskPriority.compareTwoPriorities(o1.getTaskPriorityString(), o2.getTaskPriorityString());
    if (priorityComparisonResult == 0) {
      return compareTwoTime(o1.getTimeEstimatedFinish(), o2.getTimeEstimatedFinish());
    } else {
      return priorityComparisonResult;
    }
  }

  private int compareTwoTime(Date d1, Date d2) {
    long difference = d1.getTime() - d2.getTime();
    if (difference == 0) {
      return 0;
    } else if (difference > 0) {
      return 1;
    } else {
      return -1;
    }
  }
}
