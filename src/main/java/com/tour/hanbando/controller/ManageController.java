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
  public String userList(HttpServletRequest request, Model model) {
    manageService.loadUserList(request, model);
    return "manage/userList";
  }
  
  /* 기존 회원 검색 */
  @GetMapping("/userSearchList.do")
  public String userSearchList(HttpServletRequest request, Model model) {
    manageService.loadSearchUserList(request, model);
    return "manage/userList";
  }
  
  /* 휴면 회원 목록 */
  @GetMapping("/inactiveUserList.do")
  public String inactiveUserList(HttpServletRequest request, Model model) {
    return "manage/inactiveUserList";
  }
  
  /* 휴면 회원 검색 */
  
  /* 탈퇴 회원 목록 */
  @GetMapping("/leaveUserList.do")
  public String leaveUserList(HttpServletRequest request, Model model) {
    return "manage/leaveUserList";
  }
  
  /* 탈퇴 회원 검색 */
  
  
}