package com.tour.hanbando.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tour.hanbando.service.ManageService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/manage")
@RequiredArgsConstructor
@Controller
public class ManageController {
  
  private final ManageService manageService;
  
  /* 기존 회원 목록 페이지 */
  @GetMapping("/userList.do")
  public String memberList(HttpServletRequest request, Model model) {
    manageService.loadUserList(request, model);
    return "manage/userList";
  }
  
  /* 기존 회원 검색 */
  @GetMapping("/userSearchList.do")
  public String memberSearchList(HttpServletRequest request, Model model) {
    manageService.loadSearchUserList(request, model);
    return "manage/userList";
  }
  
  
}