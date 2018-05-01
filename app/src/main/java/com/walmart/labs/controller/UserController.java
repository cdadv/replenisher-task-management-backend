package com.walmart.labs.controller;

import com.walmart.labs.domain.User;
import com.walmart.labs.dto.ResponseDTO;
import com.walmart.labs.dto.UserDTO;
import com.walmart.labs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  @Autowired private UserService userService;

  @RequestMapping(value = "/user", method = RequestMethod.GET)
  public ResponseEntity<ResponseDTO<UserDTO>> getUser() {
    ResponseDTO<UserDTO> responseDTO = new ResponseDTO<>();

    OAuth2Authentication authentication =
        (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
    User user = (User) authentication.getPrincipal();
    UserDTO userDTO = userService.getUserDTO(user);
    responseDTO.addMessage("Get user successfully!");
    responseDTO.setSuccess(true);
    responseDTO.setResult(userDTO);
    return new ResponseEntity<>(responseDTO, HttpStatus.OK);
  }

}
