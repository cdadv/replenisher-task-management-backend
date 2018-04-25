package com.walmart.labs.controller;

import com.walmart.labs.dto.ResponseDTO;
import com.walmart.labs.dto.TaskDTO;
import com.walmart.labs.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskController {
  @Autowired private TaskService taskService;

  @RequestMapping(value = "/task", method = RequestMethod.POST)
  public ResponseEntity<ResponseDTO<Void>> createQuote(@RequestBody TaskDTO taskDTO) {
    ResponseDTO<Void> responseDTO = new ResponseDTO<>();

    taskService.createTask(taskDTO);
    responseDTO.addMessage("Created a task successfully!");
    responseDTO.setSuccess(true);
    return new ResponseEntity<>(responseDTO, HttpStatus.OK);
  }
}
