package com.tour.hanbando.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tour.hanbando.dto.UserDto;
import com.tour.hanbando.service.UserService;

import lombok.RequiredArgsConstructor;

@RequestMapping(value="/user")
@RequiredArgsConstructor
@Controller
public class UserController {

  private final UserService userService;
  
  @GetMapping("/login.form")
  public String loginForm(HttpServletRequest request, Model model) throws Exception {
    // referer : 이전 주소가 저장되는 요청 Header 값
    String referer = request.getHeader("referer");
    String[] exceptUrl = {"/agree.form", "/join.form", "/join_option.form", "/find_id.form", "/find_pw.form"};
    String ret = "";
    if(referer != null) {
      for(String url : exceptUrl) {
        if(referer.contains(url)) {
          ret = request.getContextPath() + "/main.do" ; 
        }
      }
    } else {
      ret = request.getContextPath() + "/main.do" ;
    }
    
    model.addAttribute("referer", ret.isEmpty() ? referer : ret);
    // 네이버로그인-1
    model.addAttribute("naverLoginURL", userService.getNaverLoginURL(request));
    return "user/login";
  }
  
  @GetMapping("/naver/getAccessToken.do")
  public String getAccessToken(HttpServletRequest request) throws Exception {
    // 네이버로그인-2
    String accessToken = userService.getNaverLoginAccessToken(request);
    return "redirect:/user/naver/getProfile.do?accessToken=" + accessToken;
  }
  
  @GetMapping("/naver/getProfile.do")
  public String getProfile(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
    // 네이버로그인-3
    UserDto naverProfile = userService.getNaverProfile(request.getParameter("accessToken"));
    // 네이버로그인 후속 작업(처음 시도 : 간편가입, 이미 가입 : 로그인)
    UserDto user = userService.getUser(naverProfile.getEmail());
    if(user == null) {
      // 네이버 간편가입 페이지로 이동
      model.addAttribute("naverProfile", naverProfile);
      return "user/naver_join";
    } else {
      // naverProfile로 로그인 처리하기
      userService.naverLogin(request, response, null);
      return "redirect:/main.do";
    }
  }
  
  @PostMapping("/naver/join.do")
  public void naverJoin(HttpServletRequest request, HttpServletResponse response) {
    userService.naverJoin(request, response);
  }
  
  @PostMapping("/login.do")
  public void login(HttpServletRequest request, HttpServletResponse response) throws Exception {
    userService.login(request, response);
  }
  
  @GetMapping("/logout.do")
  public void logout(HttpServletRequest request, HttpServletResponse response) {
    userService.logout(request, response);
  }
}
