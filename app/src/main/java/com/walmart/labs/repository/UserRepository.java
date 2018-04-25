package com.walmart.labs.repository;

import com.walmart.labs.domain.Corporation;
import com.walmart.labs.domain.User;
import com.walmart.labs.domain.UserRole;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  List<User> findAllByIdInAndCorporationAndUserRoleListContains(
      List<Long> userIdList, Corporation corporation, UserRole userRole);
}
