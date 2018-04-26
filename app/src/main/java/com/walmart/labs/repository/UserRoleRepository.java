package com.walmart.labs.repository;

import com.walmart.labs.domain.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
  UserRole findByName(String name);
}
