package com.walmart.labs.service;

import com.walmart.labs.domain.User;
import com.walmart.labs.domain.UserRole;
import com.walmart.labs.dto.UserDTO;
import com.walmart.labs.repository.UserRepository;
import com.walmart.labs.util.ValidationService;
import java.util.Iterator;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

  @Autowired private UserRepository taskRepository;
  @Autowired private ValidationService validationService;

  public UserDTO getUserDTO(User user) {
    Set<UserRole> allowedRoleSet = user.getAllowedRoleSet();
    Iterator<UserRole> itr = allowedRoleSet.iterator();
    UserDTO userDTO = new UserDTO();
    if (itr.hasNext()) {
      userDTO.setUserId(user.getId());
      userDTO.setCorporationId(user.getCorporation().getId());
      userDTO.setFullName(user.getFullName());
      userDTO.setUsername(user.getUsername());
      userDTO.setEmailAddress(user.getEmailAddress());
      userDTO.setEnabled(user.isEnabled());
      userDTO.setDeleted(user.isDeleted());
      userDTO.setRole(itr.next().getName());
    }
    return userDTO;
  }
}
