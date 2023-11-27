package com.tour.hanbando.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tour.hanbando.service.MainService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/main")
@RequiredArgsConstructor
@Controller
public class MainController {
  private final MainService mainService;
 
  @GetMapping("search.do")
  public String mainSearch(HttpServletRequest request, Model model) {
    model.add
    return "main/search";
  }
  
  
  
}
