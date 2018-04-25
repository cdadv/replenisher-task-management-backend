package com.walmart.labs.repository;

import com.walmart.labs.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {}
