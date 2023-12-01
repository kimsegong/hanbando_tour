package com.tour.hanbando.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tour.hanbando.dto.NoticeDto;
import com.tour.hanbando.service.NoticeService;

import lombok.RequiredArgsConstructor;


@RequestMapping("/notice")
@RequiredArgsConstructor
@Controller
public class NoticeController {
  
  private final NoticeService noticeService;
  
  @GetMapping("/list.do")
  public String list(HttpServletRequest request, Model model) {
    noticeService.loadNoticeList(request, model);
    return "notice/list";
  }
  
  @PostMapping("/addNotice.do")
  public String addNotice(HttpServletRequest request, RedirectAttributes redirectAttributes) {
    int addResult = noticeService.addNotice(request);
    redirectAttributes.addFlashAttribute("addResult", addResult);
    return "redirect:/notice/list.do";
  }
  
  @GetMapping("/search.do")
  public String search(HttpServletRequest request, Model model){
    noticeService.LoadSearchList(request, model);
    return "notice/list";
  }
  
  @GetMapping("/write.form")
  public String write() {
    return "notice/write";
  }
  
  @GetMapping("/detail.do")
  public String detail(@RequestParam(value="noticeNo", required=false, defaultValue="0") int noticeNo 
      , Model model){
    NoticeDto notice = noticeService.loadNotice(noticeNo);
    model.addAttribute("notice", notice);
    return "notice/detail";
  }
  
  @PostMapping("/modifyNotice.do")
  public String modifyNotice(HttpServletRequest request, RedirectAttributes redirectAttributes) {
    int modifyResult = noticeService.modifyNotice(request);
    redirectAttributes.addFlashAttribute("modifyResult", modifyResult);
    return "redirect:/notice/detail.do?noticeNo=" + request.getParameter("noticeNo");
  }
  
  @PostMapping("/edit.form")
  public String edit() {
    return "notice/edit";
  }
  
  @PostMapping("/remove.do")
  public String remove(@RequestParam(value="noticeNo", required=false, defaultValue="0") int noticeNo
                     , RedirectAttributes redirectAttributes) {
    int removeResult = noticeService.removeNotice(noticeNo);
    redirectAttributes.addFlashAttribute("removeResult", removeResult);
    return "redirect:/notice/list.do";
  
 }
  
}
