package com.tour.hanbando.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
  
  @ResponseBody
  @GetMapping(value="/faqList.do", produces="application/json")
  public Map<String, Object> reviewList(HttpServletRequest request){
    return faqService.loadOfList(request);
  }
  
  @GetMapping("faqCash.do")
  public String faqCash(HttpServletRequest request, Model model) {
    faqService.loadFaqCashList(request, model);
    return "notice/faqCash";
  }
  
  @GetMapping("faqKorea.do")
  public String faqKorea(HttpServletRequest request, Model model) {
    faqService.loadFaqKoreaList(request, model);
    return "notice/faqKorea";
  }
  
  @GetMapping("faqMember.do")
  public String faqMember(HttpServletRequest request, Model model) {
    faqService.loadFaqMemberList(request, model);
    return "notice/faqMember";
  }
}
