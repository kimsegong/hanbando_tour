package com.tour.hanbando.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tour.hanbando.service.FaqService;
import com.tour.hanbando.service.NoticeService;

import lombok.RequiredArgsConstructor;


@RequestMapping("/notice")
@RequiredArgsConstructor
@Controller
public class FaqController {
  private final FaqService faqService;
  @GetMapping("faq.do")
  public String faq(HttpServletRequest request, Model model) {
    faqService.loadFaqList(request, model);
    return "notice/faq";
  }
}
