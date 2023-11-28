package com.tour.hanbando.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;


@RequestMapping("/notice")
@RequiredArgsConstructor
@Controller
public class FaqController {
  @GetMapping("faq.do")
  public String faq() {
    return "notice/faq";
  }
}
