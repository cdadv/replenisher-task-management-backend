package com.walmart.labs.repository;

import com.walmart.labs.domain.Corporation;
import com.walmart.labs.domain.User;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  Set<User> findAllByIdInAndCorporation(Set<Long> userIdList, Corporation corporation);

  User findByUsername(String username);
}
