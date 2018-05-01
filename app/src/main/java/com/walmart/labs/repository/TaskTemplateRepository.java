package com.walmart.labs.repository;

import com.walmart.labs.domain.Corporation;
import com.walmart.labs.domain.TaskTemplate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskTemplateRepository extends JpaRepository<TaskTemplate, Long> {
  List<TaskTemplate> findAllByCorporation(Corporation corporation);
}
