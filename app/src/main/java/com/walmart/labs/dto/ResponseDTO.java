package com.walmart.labs.dto;

import java.util.ArrayList;
import java.util.List;

public class ResponseDTO<T> {

  private List<String> messages;
  private Boolean success;
  private T result;

  public ResponseDTO() {
    this.success = false;
  }

  public ResponseDTO(Boolean success, T result, String message) {
    super();
    this.success = success;
    this.result = result;
    if (this.messages == null) {
      this.messages = new ArrayList<>();
    }
    this.messages.add(message);
  }

  public List<String> getMessages() {
    return messages;
  }

  public void setMessage(List<String> messages) {
    this.messages = messages;
  }

  public void addMessage(String message) {
    if (this.messages == null) {
      this.messages = new ArrayList<>();
    }
    this.messages.add(message);
  }

  public void addMessages(List<String> messages) {
    if (this.messages == null) {
      this.messages = new ArrayList<>();
    }
    if (messages == null) {
      return;
    }
    this.messages.addAll(messages);
  }

  public Boolean getSuccess() {
    return success;
  }

  public void setSuccess(Boolean success) {
    this.success = success;
  }

  public T getResult() {
    return result;
  }

  public void setResult(T result) {
    this.result = result;
  }
}
