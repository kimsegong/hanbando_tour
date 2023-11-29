package com.tour.hanbando.service;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;

import com.tour.hanbando.dto.UserDto;

public interface UserService {
  public void login(HttpServletRequest request, HttpServletResponse response) throws Exception;
  public String getNaverLoginURL(HttpServletRequest request) throws Exception;
  public String getNaverLoginAccessToken(HttpServletRequest request) throws Exception;
  public UserDto getNaverProfile(String accessToken) throws Exception;
  //카카오톡 로그인
  //public String getKakaoLoginURL(HttpServletRequest request) throws Exception;
  
//회원가입 시 본인 인증
//인증번호(전화번호, 인증번호)
  public Map<String, Object> certifiedPhoneNumber(String phoneNumber) throws Exception;
  

  public UserDto getUser(String email);
  public void naverJoin(HttpServletRequest request, HttpServletResponse response);
  public void naverLogin(HttpServletRequest request, HttpServletResponse response, UserDto naverProfile) throws Exception;
  public void logout(HttpServletRequest request, HttpServletResponse response);
  public ResponseEntity<Map<String, Object>> checkEmail(String email);
  public ResponseEntity<Map<String, Object>> sendCode(String email);
  public void join(HttpServletRequest request, HttpServletResponse response);
  public ResponseEntity<Map<String, Object>> modify(HttpServletRequest request);
  public void modifyPw(HttpServletRequest request, HttpServletResponse response);
  public void leave(HttpServletRequest request, HttpServletResponse response);
  public void inactiveUserBatch();
  public void active(HttpSession session, HttpServletRequest request, HttpServletResponse response);
}