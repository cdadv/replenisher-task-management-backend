package com.walmart.labs.repository.mapping;

import com.walmart.labs.domain.User;
import com.walmart.labs.domain.mapping.TaskStaffUserMapping;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskStaffUserMappingRepository extends JpaRepository<TaskStaffUserMapping, Long> {
  List<TaskStaffUserMapping> findAllByStaff(User staff);
}
