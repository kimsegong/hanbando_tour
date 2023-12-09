package com.tour.hanbando.service;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;

import com.tour.hanbando.dto.UserDto;

public interface UserService {
  public void login(HttpServletRequest request, HttpServletResponse response) throws Exception;


  
//회원가입 시 본인 인증
//인증번호(전화번호, 인증번호)
  public Map<String, Object> certifiedPhoneNumber(String phoneNumber) throws Exception;

//인증번호(비밀번호 찾기)
  public int findpw_id(String email, String mobile);
  public UserDto getUser(String email);

  
//비밀번호 찾기(인증후)
  public void doublemodifiyPw(HttpServletRequest request, HttpServletResponse response);
  //네이버
  public String getNaverLoginURL(HttpServletRequest request) throws Exception;
  public String getNaverLoginAccessToken(HttpServletRequest request) throws Exception;
  public UserDto getNaverProfile(String accessToken) throws Exception;
  public void naverJoin(HttpServletRequest request, HttpServletResponse response);
  public void naverLogin(HttpServletRequest request, HttpServletResponse response, UserDto naverProfile) throws Exception;
  
  
  //카카오톡
  public void kakaoLogin(HttpServletRequest request, HttpServletResponse response, UserDto kakaoProfile) throws Exception;
  public void kakaoJoin(HttpServletRequest request, HttpServletResponse response) throws Exception;;
  public String getKakaoLoginURL(HttpServletRequest request) throws Exception;
  public String getKakaoLoginAccessToken(HttpServletRequest request) throws Exception ;
  public UserDto getKakaoProfile(String accessToken) throws Exception;
  
  //카카오톡 간편로그인페이지에서 로그인하기
  
  public void logout(HttpServletRequest request, HttpServletResponse response);
  public ResponseEntity<Map<String, Object>> checkEmail(String email);
  public ResponseEntity<Map<String, Object>> sendCode(String email);
  public void join(HttpServletRequest request, HttpServletResponse response);
  public ResponseEntity<Map<String, Object>> modify(HttpServletRequest request);
  public void modifyPw(HttpServletRequest request, HttpServletResponse response);
  public void leave(HttpServletRequest request, HttpServletResponse response);
  public void inactiveUserBatch();
  public void active(HttpSession session, HttpServletRequest request, HttpServletResponse response);
  
  public void findId(HttpServletRequest request, HttpServletResponse response);
//아이디 찾기
  public UserDto find_id(String name, String mobile);
  
  public int autoUpdatePw90(HttpServletRequest request);
}