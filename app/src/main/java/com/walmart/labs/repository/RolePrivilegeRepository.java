package com.walmart.labs.repository;

import com.walmart.labs.domain.RolePrivilege;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolePrivilegeRepository extends JpaRepository<RolePrivilege, Long> {
  RolePrivilege findByName(String name);
}
