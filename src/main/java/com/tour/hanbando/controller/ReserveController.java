package com.tour.hanbando.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/reserve")
@Controller
public class ReserveController {

  @GetMapping("/list.do")
  public String list() {
    return "reserve/list";
  }
  
  @RequestMapping("/write.form")
  public String write() {
    return "reserve/write";
  }
  
  @RequestMapping("/detail.do")
  public String detail() {
    return "reserve/detail";
  }
  
  @RequestMapping("/edit.form")
  public String edit() {
    return "reserve/edit";
  }
  
}
