package com.walmart.labs.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;import static org.mockito.internal.verification.VerificationModeFactory.times;

import com.walmart.labs.domain.User;
import com.walmart.labs.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

  private UserController userController;

  @Mock private UserService userServiceMock;

  @Mock private User userMock;

  @Before
  public void before() {
    userController = new UserController();
  }

  @Test
  public void testGetUser() {
    UserService userServiceMock = Mockito.mock(UserService.class);
    userController.getUser();
    Mockito.verify(userServiceMock, times(1)).getUserDTO(any());
  }
}