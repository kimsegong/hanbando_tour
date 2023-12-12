package com.tour.hanbando.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tour.hanbando.dto.InquiryAnswerDto;
import com.tour.hanbando.dto.InquiryDto;
import com.tour.hanbando.service.InquiryService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/notice")
@RequiredArgsConstructor
@Controller
public class InquiryController {

  private final InquiryService inquiryService;
  
  /*회원이 보는 1:1문의목록*/
  @GetMapping("/inquirylist.do")
  public String inquirylist(@RequestParam(value="userNo", required=false, defaultValue="0") int userNo, Model model) {
    inquiryService.loadUserInquiryList(userNo, model);
    return "notice/inquirylist";
  }
  /* 관리자가 보는 1:1문의목록 */
  @GetMapping("/inquiryManage.do")
  public String inquiryManage(HttpServletRequest request, Model model) {
    inquiryService.loadInquiryList(request, model);
    return "notice/inquirylistManage";
  }
  
  /* 1:1문의 작성하기 */
  @PostMapping("/addInquiry.do")
  public String addInquiry(HttpServletRequest request, RedirectAttributes redirectAttributes) {
    int addResult = inquiryService.addInquiry(request);
    redirectAttributes.addFlashAttribute("addResult", addResult);
    return "redirect:/notice/inquirylist.do?userNo=" +request.getParameter("userNo");
  }
  
  @GetMapping("/inquirywrite.form")
  public String inquirywrite() {
    return "notice/inquirywrite";
  
  }
  
  @PostMapping("/inquiryremove.do")
  public String inquiryremove(@RequestParam(value="inquiryNo", required=false, defaultValue="0") int inquiryNo
                     , RedirectAttributes redirectAttributes) {
    int removeResult = inquiryService.removeInquiry(inquiryNo);
    redirectAttributes.addFlashAttribute("removeResult", removeResult);
    return "redirect:/notice/inquiryManage.do";
  
 }
  
  @GetMapping("/inquirydetail.do")
  public String inquirydetail(@RequestParam(value="inquiryNo", required=false, defaultValue="0") int inquiryNo 
      , Model model){
    InquiryDto inquiry = inquiryService.loadInquiry(inquiryNo);
    model.addAttribute("inquiry", inquiry);
    InquiryAnswerDto answer = inquiryService.loadInquiryAnswer(inquiryNo);
    model.addAttribute("answer", answer);
    return "notice/inquirydetail";
  }
  
  @PostMapping("/modifyInquiry.do")
  public String modifyInquiry(HttpServletRequest request, RedirectAttributes redirectAttributes) {
    int modifyResult = inquiryService.modifyInquiry(request);
    redirectAttributes.addFlashAttribute("modifyResult", modifyResult);
    return "redirect:/notice/inquirydetail.do?inquiryNo=" + request.getParameter("inquiryNo");
  }
  
  /* 1:1문의 작성하기 */
  @PostMapping("/addInquiryAnswer.do")
  public String addInquiryAnswer(HttpServletRequest request, RedirectAttributes redirectAttributes) {
    int addResult = inquiryService.addInquiryAnswer(request);
    redirectAttributes.addFlashAttribute("addResult", addResult);
    return "redirect:/notice/inquirydetail.do?inquiryNo=" + request.getParameter("inquiryNo");
  }
  
  
  @PostMapping("/inquiryAnswerWrite.form")
  public String inquiryAnswerWrite(HttpServletRequest request, Model model) {
    model.addAttribute("inquiryNo", request.getParameter("inquiryNo"));
    return "notice/inquiryAnswerWrite";
  }
  
  /* 문의 검색 */
  @GetMapping("/searchInquiryList.do")
  public String searchInquiryList(HttpServletRequest request, Model model) {
    inquiryService.loadSearchInquiryList(request, model);
    return "notice/inquirylistManage";
  }
}
