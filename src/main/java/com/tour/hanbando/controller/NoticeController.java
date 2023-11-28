package com.tour.hanbando.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;


@RequestMapping("/notice")
@RequiredArgsConstructor
@Controller
public class NoticeController {
  
  @GetMapping("/list.do")
  public String list() {
    return "notice/list";
  }
  
  @PostMapping("/edit.form")
  public String edit() {
    return "notice/edit";
  }
  
  @GetMapping("detail.do")
  public String detail() {
    return "notice/detail";
  }
  
  @GetMapping("write.form")
  public String write() {
    return "notice/write";
  }
  
  
  
  
  
}
