package com.tour.hanbando.controller;


import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.Spring;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tour.hanbando.dto.UserDto;
import com.tour.hanbando.service.UserService;

import lombok.RequiredArgsConstructor;

@RequestMapping(value="/user")
@RequiredArgsConstructor
@Controller
public class UserController {
  
  private final UserService userService;
  
 //인증번호
  @ResponseBody
  @GetMapping(value="/execute.form", produces="application/json")
  public Map<String, Object> sendSMS(@RequestParam String userPhoneNum) throws Exception {
      // 문자 보내기
      return userService.certifiedPhoneNumber(userPhoneNum);
      // 인증번호 반환 {"cerNum": 12345}
  }


  
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
    // 카카오로그인-1
    model.addAttribute("kakaoLoginURL", userService.getKakaoLoginURL(request));
    return "user/login";
  }
  
  /////////////////네이버 로그인////////////////////////////////
  
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
      userService.naverLogin(request, response, naverProfile);
      return "redirect:/main.do";
    }
  }
  
  @PostMapping("/naver/join.do")
  public void naverJoin(HttpServletRequest request, HttpServletResponse response) {
    userService.naverJoin(request, response);
  }
  
  ///////////////////카카오 로그인////////////////////////
  
  @PostMapping("/kakao/join.do")
  public void kakaoJoin(HttpServletRequest request, HttpServletResponse response) throws Exception {
    userService.kakaoJoin(request, response);
  }
  
  @GetMapping("/kakao/getAccessToken.do")
  public String getKakaoAccessToken(HttpServletRequest request) throws Exception {
    String accessToken = userService.getKakaoLoginAccessToken(request);
    return "redirect:/user/kakao/getProfile.do?accessToken=" + accessToken;
  }
  
  //카카오 토큰 발급
  @GetMapping("/kakao/getProfile.do")
  public String getKakaoProfile(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
    UserDto kakaoProfile = userService.getKakaoProfile(request.getParameter("accessToken"));
    UserDto user = userService.getUser(kakaoProfile.getEmail());
    
    if(user == null) {
      // 카카오 간편가입 페이지로 이동
      model.addAttribute("kakaoProfile", kakaoProfile);
      return "user/kakao_join";
    } else {
      // kakaoProfile로 로그인 처리하기
      userService.kakaoLogin(request, response, kakaoProfile);
      return "redirect:/main.do";
    }
  }
    
  
  
  
  
  @PostMapping("/login.do")
  public void login(HttpServletRequest request, HttpServletResponse response) throws Exception {
    userService.login(request, response);
  }
  
  @GetMapping("/logout.do")
  public void logout(HttpServletRequest request, HttpServletResponse response) {
    userService.logout(request, response);
  }
  
  @GetMapping("/agree.form")
  public String agreeForm() {
    return "user/agree";
  }
  
  @GetMapping("/join.form")
  public String joinForm(@RequestParam(value="service", required=false, defaultValue="off") String service
                       , @RequestParam(value="event", required=false, defaultValue="off") String event
                       , Model model) {
    String rtn = null;
    if(service.equals("off")) {
      rtn = "redirect:/main.do";
    } else {
      model.addAttribute("event", event);  // user 폴더 join.jsp로 전달하는 event는 "on" 또는 "off" 값을 가진다.
      rtn = "user/join";
    }
    return rtn;
  }
  
  @GetMapping(value="/checkEmail.do", produces=MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, Object>> checkEmail(@RequestParam String email) {
    return userService.checkEmail(email);
  }
  
  @GetMapping(value="/sendCode.do", produces=MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, Object>> sendCode(@RequestParam String email) {
    return userService.sendCode(email);
  }
  
  @PostMapping("/join.do")
  public void join(HttpServletRequest request, HttpServletResponse response) {
    userService.join(request, response);
  }
  
  @GetMapping("/mypage.form")
  public String mypageForm() {
    return "user/mypage";
  }
  
  @PostMapping(value="/modify.do", produces=MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, Object>> modify(HttpServletRequest request) {
    return userService.modify(request);
  }
  
  @GetMapping("/modifyPw.form")
  public String modifyPwForm() {
    return "user/pw";
  }
  
  @PostMapping("/modifyPw.do")
  public void modifyPw(HttpServletRequest request, HttpServletResponse response) {
    userService.modifyPw(request, response);
  }
  
  @PostMapping("/leave.do")
  public void leave(HttpServletRequest request, HttpServletResponse response) {
    userService.leave(request, response);
  }
  
  @GetMapping("/active.form")
  public String activeForm() {
    return "user/active";
  }
  
  @GetMapping("/active.do")
  public void active(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
    userService.active(session, request, response);
  }
  
  
  
  
  
}
  