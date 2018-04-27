package com.walmart.labs.repository;

import com.walmart.labs.domain.Corporation;
import com.walmart.labs.domain.Task;
import com.walmart.labs.domain.User;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
  Task findByNameAndCorporationAndStaffSet(
      String name, Corporation corporation, Set<User> staffSet);
}
