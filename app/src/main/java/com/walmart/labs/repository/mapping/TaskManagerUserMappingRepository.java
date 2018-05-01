package com.walmart.labs.repository.mapping;

import com.walmart.labs.domain.User;
import com.walmart.labs.domain.mapping.TaskManagerUserMapping;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskManagerUserMappingRepository
    extends JpaRepository<TaskManagerUserMapping, Long> {
  List<TaskManagerUserMapping> findAllByManager(User manager);
}
