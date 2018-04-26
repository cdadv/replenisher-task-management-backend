package com.walmart.labs.util;

import org.springframework.security.crypto.password.PasswordEncoder;

public class SimplePasswordEncoder implements PasswordEncoder {

  @Override
  public String encode(CharSequence rawPassword) {
    return rawPassword.toString();
  }

  @Override
  public boolean matches(CharSequence rawPassword, String encodedPassword) {
    return false;
  }
}
