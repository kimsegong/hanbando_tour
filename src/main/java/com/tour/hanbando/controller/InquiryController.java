package com.tour.hanbando.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tour.hanbando.service.InquiryService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/notice")
@RequiredArgsConstructor
@Controller
public class InquiryController {

  private final InquiryService inquiryService;
  
  @GetMapping("/inquirylist.do")
  public String inquirylist(HttpServletRequest request, Model model) {
    inquiryService.loadInquiryList(request, model);
    return "notice/inquirylist";
  }
  
  @PostMapping("/addInquiry.do")
  public String addInquiry(HttpServletRequest request, RedirectAttributes redirectAttributes) {
    int addResult = inquiryService.addInquiry(request);
    redirectAttributes.addFlashAttribute("addResult", addResult);
    return "redirect:/notice/inquirylist.do";
  }
  
  @GetMapping("/inquirywrite.form")
  public String inquirywrite() {
    return "notice/inquirywrite";
  
  }

}
