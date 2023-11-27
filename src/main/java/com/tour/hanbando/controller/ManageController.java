package com.tour.hanbando.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tour.hanbando.service.ManageService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/manage")
@RequiredArgsConstructor
@Controller
public class ManageController {
  
  private final ManageService manageService;
  

  
}