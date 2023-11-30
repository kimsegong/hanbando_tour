package com.tour.hanbando.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tour.hanbando.service.InquiryService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/notice")
@RequiredArgsConstructor
@Controller
public class InquiryController {

  private final InquiryService inquiryService;
  
  @GetMapping("/inquirylist.do")
  public String inquirylist() {
    return "notice/inquirylist";
  }
  
  @PostMapping("/addInquiry.do")
  public String addInquiry() {
    return "redirect:/notice/inquirylist.do";
  }

}
