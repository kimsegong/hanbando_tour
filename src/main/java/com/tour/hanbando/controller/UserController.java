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
  @GetMapping("/execute.form")
  public String sendSMS(String userPhoneNum) {
      // 5자리 인증번호 만들기
      Random random  = new Random();
      String numStr = "";
      for(int i=0; i<5; i++) {
          String ranNum = Integer.toString(random.nextInt(10));   // 0부터 9까지 랜덤으로 숫자를 뽑는다.
          numStr += ranNum;   // 랜덤으로 나온 숫자를 하나씩 누적해서 담는다.
      }
      // 확인용
      System.out.println("수신자 번호 : " + userPhoneNum);
      System.out.println("인증번호 : " + numStr);

      // 문자 보내기
     // userService.certifiedPhoneNumber(userPhoneNum , numStr);
      return numStr;    // 인증번호 반환
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
    //model.addAttribute("kakaoLoginURL", userService.getKakaoLoginURL(request));
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
      userService.naverLogin(request, response, naverProfile);
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
  