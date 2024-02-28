package com.tour.hanbando.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.tour.hanbando.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class InactiveUserBatch {

  private final UserService userService;
  
  @Scheduled(cron="0 0 0 1/1 * ?")  
  public void execute() {
    userService.inactiveUserBatch();
  }
  
}