package com.tour.hanbando.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequestMapping("/notice")
@RequiredArgsConstructor
@Controller
public class InquiryController {
    @GetMapping("inquiry.form")
    public String inquiry() {
      return "notice/inquiry";
    }
    
    @GetMapping("inquirylist.do")
    public String inquirylist() {
      return "notice/inquirylist";
    }
  
}
