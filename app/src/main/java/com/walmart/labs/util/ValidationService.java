package com.walmart.labs.util;

import org.springframework.stereotype.Service;

@Service
public class ValidationService {
  public boolean validateTextField(String text) {
    // 65535 is the maximum length defined for domain property 'description', 'note', 'feedback' */
    return text.length() <= 65535;
  }
}
