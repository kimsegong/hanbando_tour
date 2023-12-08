package com.tour.hanbando.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tour.hanbando.dao.UserMapper;
import com.tour.hanbando.dto.InactiveUserDto;
import com.tour.hanbando.dto.UserDto;
import com.tour.hanbando.util.MyJavaMailUtils;
import com.tour.hanbando.util.MySecurityUtils;

import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;

@Transactional
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

  private final UserMapper userMapper;
  private final MySecurityUtils mySecurityUtils;
  private final MyJavaMailUtils myJavaMailUtils;

  
  private final String client_id = "dteUoZxabIKjJ8XhKGY0";
  private final String client_secret = "hzj3TKHiSm";
  
  
  private final String ka_Client_id = "9ac35c110f888ef0213ea4dbd3fab619";
  

  private DefaultMessageService messageService;
  
 
  //인증번호(회원가입)
  
  @Override
  public Map<String, Object> certifiedPhoneNumber(String phoneNumber) throws Exception {
    
      String api_key = "NCS9YET2CLIIXCUL";
      String api_secret = "CTWEHCPFCPLEM2AVYQY02UDN8LXROBCQ";
      messageService = NurigoApp.INSTANCE.initialize(api_key, api_secret,"https://api.coolsms.co.kr");
      
      int cerNum = Integer.parseInt(mySecurityUtils.getRandomString(5, false, true));
      
      Message message = new Message();
      // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
      message.setFrom("01062316858");
      message.setTo(phoneNumber);
      message.setText("[한반도투어] 본인확인 인증번호는 [" + cerNum + "] 입니다.");
      
      
      SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
      System.out.println(response);
      
      return Map.of("cerNum", cerNum);
      
  }
    
  //인증번호(비밀번호변경)

  
  @Override
  public int findpw_id(String email, String mobile) {
    UserDto result = null;
    
    
     result= userMapper.getFindPw(Map.of("email", email, "mobile", mobile));
     int user = 0;
     if(result == null) {
       user = 0;
     } else if(result != null) {
       user = 1;
     }
   
    
    return user;
  
}
  
  
 //비밀번호찾기(인증후)
  @Override
  public void doublemodifiyPw(HttpServletRequest request, HttpServletResponse response) {
   
    String pw = mySecurityUtils.getSHA256(request.getParameter("pw"));
    String email = request.getParameter("email");
    
    UserDto user = UserDto.builder()
                    .pw(pw)
                    .email(email)
                    .build();
    
    int modifyPwResult = userMapper.modifiedUserPw(user);
    
    try {
      
      response.setContentType("text/html; charset=UTF-8");
      PrintWriter out = response.getWriter();
      out.println("<script>");
      if(modifyPwResult == 1) {
        out.println("alert('비밀번호가 수정되었습니다.')");
        out.println("location.href='" + request.getContextPath() + "/user/login.form'");
      } else {
        out.println("alert('비밀번호가 수정되지 않았습니다.')");
        out.println("history.back()");
      }
      out.println("</script>");
      out.flush();
      out.close();
      
    } catch (Exception e) {
      e.printStackTrace();
    }
    
  }
  
    
    
    
    
    
  
  
  @Override
  public void login(HttpServletRequest request, HttpServletResponse response) throws Exception {
    
    String email = request.getParameter("email");
    String pw = mySecurityUtils.getSHA256(request.getParameter("pw"));
    
    Map<String, Object> map = Map.of("email", email
                                   , "pw", pw);

    HttpSession session = request.getSession();
    
 // 휴면 계정인지 확인하기
    InactiveUserDto inactiveUser = userMapper.getInactiveUser(map);
    if(inactiveUser != null) {
      session.setAttribute("inactiveUser", inactiveUser);
      response.sendRedirect(request.getContextPath() + "/user/active.form");
      

    }
    
    // 정상적인 로그인 처리하기
    UserDto user = userMapper.getUser(map);
    //int checkDayOfPwModifiedAt = userMapper.recentpWChange(map); // 여기에 경과일수 int 타입으로 반환
    
    if(user != null) {
      session.setAttribute("user", user);
      userMapper.insertAccess(email);
    
      // 비밀번호 변경 90일 지나면 알림      
      boolean userPW90 = userMapper.changePw90(email) == null;

        if (!userPW90 ) {
        response.setContentType("text/html; charset=UTF-8");
          PrintWriter outt = response.getWriter();
          outt.println("<script>");
          outt.println("alert('마지막 비밀번호 변경일로부터 90일이 경과했습니다. 비밀번호를 변경해주세요.')");
          outt.println("location.href='" +  "/mypage/modifyPw.form'");
          outt.println("</script>");
          outt.flush();
          outt.close();
      
      
      
          response.sendRedirect(request.getParameter("referer"));
    } else {
   // 90일 이전인 경우에 실행할거 있으면 적기  
    }
      response.setContentType("text/html; charset=UTF-8");
      PrintWriter out = response.getWriter();
      out.println("<script>");
      out.println("alert('일치하는 회원 정보가 없습니다.')");
      out.println("location.href='" + request.getContextPath() + "/main.do'");
      out.println("</script>");
      out.flush();
      out.close();
    }
  
  }
  
  @Override
  public int autoUpdatePw90(HttpServletRequest request) {
    int userNo = Integer.parseInt(request.getParameter("userNo"));
    UserDto user = UserDto.builder()
                        .userNo(userNo)
                        .build();
     int autoUpdatePw90Result = userMapper.autoupdatetmpPw(user);  
     return autoUpdatePw90Result;
  }
  
  ////////////////네이버/////////////////////////
  //네이버 로그인1
  @Override
    public String getNaverLoginURL(HttpServletRequest request) throws Exception {
    String apiURL = "https://nid.naver.com/oauth2.0/authorize";
    String response_type = "code";
    String redirect_uri = URLEncoder.encode("http://localhost:8080" + "/user/naver/getAccessToken.do", "UTF-8");
    String state = new BigInteger(130, new SecureRandom()).toString();

    StringBuilder sb = new StringBuilder();
    sb.append(apiURL);
    sb.append("?response_type=").append(response_type);
    sb.append("&client_id=").append(client_id);
    sb.append("&redirect_uri=").append(redirect_uri);
    sb.append("&state=").append(state);
    
    return sb.toString();
    }
  
  //네이버 로그인2
  @Override
  public String getNaverLoginAccessToken(HttpServletRequest request) throws Exception {

    String code = request.getParameter("code");
    String state = request.getParameter("state");
    
    String apiURL = "https://nid.naver.com/oauth2.0/token";
    String grant_type = "authorization_code"; 
    
    StringBuilder sb = new StringBuilder();
    sb.append(apiURL);
    sb.append("?grant_type=").append(grant_type);
    sb.append("&client_id=").append(client_id);
    sb.append("&client_secret=").append(client_secret);
    sb.append("&code=").append(code);
    sb.append("&state=").append(state);
    
    // 요청
    URL url = new URL(sb.toString());
    HttpURLConnection con = (HttpURLConnection)url.openConnection();
    con.setRequestMethod("GET");  // 반드시 대문자로 작성
    
    // 응답
    BufferedReader reader = null;
    int responseCode = con.getResponseCode();
    if(responseCode == 200) {
      reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
    } else {
      reader = new BufferedReader(new InputStreamReader(con.getErrorStream()));
    }
    
    String line = null;
    StringBuilder responseBody = new StringBuilder();
    while ((line = reader.readLine()) != null) {
      responseBody.append(line);
    }
    
    JSONObject obj = new JSONObject(responseBody.toString());
    return obj.getString("access_token");
  }
  
  // 네이버 로그인 3
  @Override
  public UserDto getNaverProfile(String accessToken) throws Exception {
  
    String apiURL = "https://openapi.naver.com/v1/nid/me";
    URL url = new URL(apiURL);
    HttpURLConnection con = (HttpURLConnection)url.openConnection();
    con.setRequestMethod("GET");
    con.setRequestProperty("Authorization", "Bearer " + accessToken);
    
    // 응답
    BufferedReader reader = null;
    int responseCode = con.getResponseCode();
    if(responseCode == 200) {
      reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
    } else {
      reader = new BufferedReader(new InputStreamReader(con.getErrorStream()));
    }
    
    String line = null;
    StringBuilder responseBody = new StringBuilder();
    while ((line = reader.readLine()) != null) {
      responseBody.append(line);
    }
    
    // 응답 결과(프로필을 JSON으로 응답) -> UserDto 객체
    JSONObject obj = new JSONObject(responseBody.toString());
    JSONObject response = obj.getJSONObject("response");
    UserDto user = UserDto.builder()
                    .email(response.getString("email"))
                    .name(response.getString("name"))
                    .gender(response.getString("gender"))
                    .mobile(response.getString("mobile"))
                    .build();
    
    return user;
  }
  
  @Override
  public void naverJoin(HttpServletRequest request, HttpServletResponse response) {
    
    String email = request.getParameter("email");
    String name = request.getParameter("name");
    String gender = request.getParameter("gender");
    String mobile = request.getParameter("mobile");
    String event = request.getParameter("event");
    
    UserDto user = UserDto.builder()
                        .email(email)
                        .name(name)
                        .gender(gender)
                        .mobile(mobile.replace("-", ""))
                        .agree(event != null ? 1 : 0)
                        .build();
    
    int naverJoinResult = userMapper.insertNaverUser(user);

    try {
      
      response.setContentType("text/html; charset=UTF-8");
      PrintWriter out = response.getWriter();
      out.println("<script>");
      if(naverJoinResult == 1 ) {
        request.getSession().setAttribute("usre", userMapper.getUser(Map.of("email", email)));
        userMapper.insertAccess(email);
        out.println("alert('네이버 간편 가입이 완료 되었습니다.')");
      } else {
        out.println("alert('네이버 간편 가입이 실패했습니다.')");
      }
      out.println("location.href='" + "/main.do'");
      out.println("</script>");
      out.flush();
      out.close();
      
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  @Override
  public void naverLogin(HttpServletRequest request, HttpServletResponse response, UserDto naverProfile) throws Exception {
    
    String email = naverProfile.getEmail();
    UserDto user = userMapper.getUser(Map.of("email", email));
    
    if(user != null) {
      request.getSession().setAttribute("user", user);
      userMapper.insertAccess(email);
    } else {
      response.setContentType("text/html; charset=UTF-8");
      PrintWriter out = response.getWriter();
      out.println("<script>");
      out.println("alert('일치하는 회원 정보가 없습니다.')");
      out.println("location.href='" + request.getContextPath() + "/main.do'");
      out.println("</script>");
      out.flush();
      out.close();
    }
  }
  
  

  
  
  @Override
  public UserDto getUser(String email) {
    return userMapper.getUser(Map.of("email", email));
  }
  
  // 카카오 가입
  @Override
  public void kakaoJoin(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String mobile = request.getParameter("mobile");
    String email = request.getParameter("email");
  //  String pw = request.getParameter("pw");
    String name = request.getParameter("name");
    String gender = request.getParameter("gender");
    
 // Check if mobile is null before invoking replace
    if (mobile != null) {
        // Perform the replace operation or handle it as needed
        //mobile = mobile.replace(oldChar, newChar);
    } else {
        // Handle the case where mobile is null, log an error, or throw an exception
        // For example:
        throw new IllegalArgumentException("Mobile cannot be null");
    }
    
    
    
    
    UserDto user = UserDto.builder()    
                          .mobile(mobile)
                          .email(email)
  //                       .pw(pw)
                          .name(name)
                          .gender(gender)
                          .build();
    
    int kakaoJoinResult = userMapper.kakaoJoin(user);
    
  try {
      
      response.setContentType("text/html; charset=UTF-8");
      PrintWriter out = response.getWriter();
      out.println("<script>");
      if(kakaoJoinResult == 1) {
        request.getSession().setAttribute("user", userMapper.getUser(Map.of("email", email)));
        userMapper.insertAccess(email);
        out.println("alert('카카오 간편가입이 완료되었습니다.')");
      } else {
        out.println("alert('카카오 간편가입이 실패했습니다.')");
      }
      out.println("location.href='" + request.getContextPath() + "/main.do'");
      out.println("</script>");
      out.flush();
      out.close();
      
    } catch (Exception e) {
      e.printStackTrace();
    }
    
  }
 
  
  // 카카오 로그인1.. 
  @Override
  public String getKakaoLoginURL(HttpServletRequest request) throws Exception {
      String apiURL = "https://kauth.kakao.com/oauth/authorize";
      String response_type = "code";
      String redirect_uri = URLEncoder.encode("http://localhost:8080/" + "user/kakao/getAccessToken.do", "UTF-8");
      String state = new BigInteger(130, new SecureRandom()).toString();

      StringBuilder sb = new StringBuilder();
      sb.append(apiURL);
      sb.append("?response_type=").append(response_type);
      sb.append("&client_id=").append(ka_Client_id);
      sb.append("&redirect_uri=").append(redirect_uri);
      sb.append("&state=").append(state);

      return sb.toString();
  }

  // 카카오 로그인2..
  @Override
  public String getKakaoLoginAccessToken(HttpServletRequest request) throws Exception {
    
    String apiURL ="https://kauth.kakao.com/oauth/token";
    String grant_type = "authorization_code";
    
    String code =request.getParameter("code");
    String state =request.getParameter("state");
    
    StringBuilder sb = new StringBuilder();
    sb.append(apiURL);
    sb.append("?grant_type=").append(grant_type);
    sb.append("&client_id=").append(ka_Client_id);
    sb.append("&code=").append(code);
    sb.append("&state=").append(state);
    
    // 요청
    URL url = new URL(sb.toString());
    HttpURLConnection con = (HttpURLConnection)url.openConnection();
    con.setRequestMethod("GET"); 
    
    // 응답
    BufferedReader reader = null;
    int responseCode = con.getResponseCode();
    if(responseCode == 200) {
      reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
    } else {
      reader = new BufferedReader(new InputStreamReader(con.getErrorStream()));
    }
    
    String line = null;
    StringBuilder responseBody = new StringBuilder();
    while ((line = reader.readLine()) != null) {
      responseBody.append(line);
    }
    
    JSONObject obj = new JSONObject(responseBody.toString());
    return obj.getString("access_token");
  }
  
  // 카카오 3..
  //https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#req-user-info
  @Override
  public UserDto getKakaoProfile(String accessToken) throws Exception {
      String apiURL = "https://kapi.kakao.com/v2/user/me";
      URL url = new URL(apiURL);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      con.setRequestMethod("GET");
      con.setRequestProperty("Authorization", "Bearer " + accessToken);

      // 응답
      try (BufferedReader reader = new BufferedReader(new InputStreamReader(con.getResponseCode() == 200 ? con.getInputStream() : con.getErrorStream()))) {
          String line;
          StringBuilder responseBody = new StringBuilder();
          while ((line = reader.readLine()) != null) {
              responseBody.append(line);
          }

          JSONObject obj = new JSONObject(responseBody.toString());

          // Check if the key "kakao_account" exists
          if (obj.has("kakao_account")) {
              JSONObject kakaoAccount = obj.getJSONObject("kakao_account");

              // Check if the key "name" exists
              String name = kakaoAccount.has("profile") ? kakaoAccount.getJSONObject("profile").getString("nickname") : null;

              // Check if the key "email" exists
              String email = kakaoAccount.has("email") ? kakaoAccount.getString("email") : null;

              // Check if the key "gender" exists
              String gender = kakaoAccount.has("gender") ? kakaoAccount.getString("gender") : null;

              // Check if the key "phone_number" exists
              String phoneNumber = kakaoAccount.has("phone_number") ? kakaoAccount.getString("phone_number") : null;

              UserDto user = UserDto.builder()
                      .email(email)
                      .name(name)
                      .gender(gender)
                      .mobile(phoneNumber)
                      .build();

              return user;
          } else {
              // Handle the case where "kakao_account" key is not found
              throw new JSONException("Key 'kakao_account' not found in the JSON response");
          }
      } catch (IOException e) {
          // Handle IOException if necessary
          throw new Exception("Error reading from connection", e);
      }
  }
  // 카카오 로그인
  @Override
  public void kakaoLogin(HttpServletRequest request, HttpServletResponse response, UserDto kakaoProfile)
      throws Exception {
    String email = kakaoProfile.getEmail();
    UserDto user = userMapper.getUser(Map.of("email", email));
    
    if(user != null) {
      request.getSession() .setAttribute("user", user);
      userMapper.insertAccess(email);
      } else {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<script>");
        out.println("alert('일치하는 회원 정보가 없습니다.')");
        out.println("location.href='" + request.getContextPath() + "/main.do'");
        out.println("</script>");
        out.flush();
        out.close();
      }
  }


  
  @Override
  public void logout(HttpServletRequest request, HttpServletResponse response) {
    
    HttpSession session = request.getSession();
    
    session.invalidate();
    
    try {
      response.sendRedirect(request.getContextPath() + "/main.do");
    } catch (Exception e) {
      e.printStackTrace();
    }
    
  }
  
  @Transactional(readOnly=true)
  @Override
  public ResponseEntity<Map<String, Object>> checkEmail(String email) {
    
    Map<String, Object> map = new HashMap<>();
    map.put("email", email);
    
    boolean enableEmail = userMapper.getUser(map) == null
                       && userMapper.getLeaveUser(map) == null
                       && userMapper.getInactiveUser(map) == null;
    
    return new ResponseEntity<>(Map.of("enableEmail", enableEmail), HttpStatus.OK);
    
  }
  
  @Override
  public ResponseEntity<Map<String, Object>> sendCode(String email) {
    
    // RandomString 생성(6자리, 문자 사용, 숫자 사용)
    String code = mySecurityUtils.getRandomString(6, true, true);
    
    // 메일 전송
    myJavaMailUtils.sendJavaMail(email
                               , "myhome 인증 코드"
                               , "<div>인증코드는 <strong>" + code + "</strong>입니다.</div>");
    
    return new ResponseEntity<>(Map.of("code", code), HttpStatus.OK);
    
  }
  
  @Override
  public void join(HttpServletRequest request, HttpServletResponse response) {
    
    String email = request.getParameter("email");
    String pw = mySecurityUtils.getSHA256(request.getParameter("pw"));
    String name = mySecurityUtils.preventXSS(request.getParameter("name"));
    String gender = request.getParameter("gender");
    String mobile = request.getParameter("mobile");
    String postcode = request.getParameter("postcode");
    String roadAddress = request.getParameter("roadAddress");
    String jibunAddress = request.getParameter("jibunAddress");
    String detailAddress = mySecurityUtils.preventXSS(request.getParameter("detailAddress"));
    String event = request.getParameter("event");
    
    UserDto user = UserDto.builder()
                    .email(email)
                    .pw(pw)
                    .name(name)
                    .gender(gender)
                    .mobile(mobile)
                    .postcode(postcode)
                    .roadAddress(roadAddress)
                    .jibunAddress(jibunAddress)
                    .detailAddress(detailAddress)
                    .agree(event.equals("on") ? 1 : 0)
                    .build();
    
    int joinResult = userMapper.insertUser(user);
    
    try {
      
      response.setContentType("text/html; charset=UTF-8");
      PrintWriter out = response.getWriter();
      out.println("<script>");
      if(joinResult == 1) {
        request.getSession().setAttribute("user", userMapper.getUser(Map.of("email", email)));
        userMapper.insertAccess(email);
        out.println("alert('회원 가입되었습니다.')");
        out.println("location.href='" + request.getContextPath() + "/main.do'");
      } else {
        out.println("alert('회원 가입이 실패했습니다.')");
        out.println("history.go(-2)");
      }
      out.println("</script>");
      out.flush();
      out.close();
      
    } catch (Exception e) {
      e.printStackTrace();
    }
    
  }

  @Override
  public ResponseEntity<Map<String, Object>> modify(HttpServletRequest request) {
    
    String name = mySecurityUtils.preventXSS(request.getParameter("name"));
    String gender = request.getParameter("gender");
    String mobile = request.getParameter("mobile");
    String postcode = request.getParameter("postcode");
    String roadAddress = request.getParameter("roadAddress");
    String jibunAddress = request.getParameter("jibunAddress");
    String detailAddress = mySecurityUtils.preventXSS(request.getParameter("detailAddress"));
    String event = request.getParameter("event");
    int agree = event.equals("on") ? 1 : 0;
    int userNo = Integer.parseInt(request.getParameter("userNo"));
    
    UserDto user = UserDto.builder()
        .name(name)
        .gender(gender)
        .mobile(mobile)
        .postcode(postcode)
        .roadAddress(roadAddress)
        .jibunAddress(jibunAddress)
        .detailAddress(detailAddress)
        .agree(agree)
        .userNo(userNo)
        .build();
    
    int modifyResult = userMapper.updateUser(user);
    
    if(modifyResult == 1) {
      HttpSession session = request.getSession();
      UserDto sessionUser = (UserDto)session.getAttribute("user");
      sessionUser.setName(name);
      sessionUser.setGender(gender);
      sessionUser.setMobile(mobile);
      sessionUser.setPostcode(postcode);
      sessionUser.setRoadAddress(roadAddress);
      sessionUser.setJibunAddress(jibunAddress);
      sessionUser.setDetailAddress(detailAddress);
      sessionUser.setAgree(agree);
    }
    
    return new ResponseEntity<>(Map.of("modifyResult", modifyResult), HttpStatus.OK);
    
  }

  @Override
  public void modifyPw(HttpServletRequest request, HttpServletResponse response) {
    
    String pw = mySecurityUtils.getSHA256(request.getParameter("pw"));
    int userNo = Integer.parseInt(request.getParameter("userNo"));
    
    UserDto user = UserDto.builder()
                    .pw(pw)
                    .userNo(userNo)
                    .build();
    
    int modifyPwResult = userMapper.updateUserPw(user);
    
    try {
      
      response.setContentType("text/html; charset=UTF-8");
      PrintWriter out = response.getWriter();
      out.println("<script>");
      if(modifyPwResult == 1) {
        HttpSession session = request.getSession();
        UserDto sessionUser = (UserDto)session.getAttribute("user");
        sessionUser.setPw(pw);
        out.println("alert('비밀번호가 수정되었습니다.')");
        out.println("location.href='" + request.getContextPath() + "/user/mypage.form'");
      } else {
        out.println("alert('비밀번호가 수정되지 않았습니다.')");
        out.println("history.back()");
      }
      out.println("</script>");
      out.flush();
      out.close();
      
    } catch (Exception e) {
      e.printStackTrace();
    }
    
  }
  
  @Override
  public void leave(HttpServletRequest request, HttpServletResponse response) {
  
    Optional<String> opt = Optional.ofNullable(request.getParameter("userNo"));
    int userNo = Integer.parseInt(opt.orElse("0"));
    
    UserDto user = userMapper.getUser(Map.of("userNo", userNo));
    
    if(user == null) {
      try {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<script>");
        out.println("alert('회원 탈퇴를 수행할 수 없습니다.')");
        out.println("location.href='" + request.getContextPath() + "/main.do'");
        out.println("</script>");
        out.flush();
        out.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    
    int insertLeaveUserResult = userMapper.insertLeaveUser(user);
    int deleteUserResult = userMapper.deleteUser(user);
    
   try {
      
      response.setContentType("text/html; charset=UTF-8");
      PrintWriter out = response.getWriter();
      out.println("<script>");
      if(insertLeaveUserResult == 1 && deleteUserResult == 1) {
        HttpSession session = request.getSession();
        session.invalidate();
        out.println("alert('회원 탈퇴되었습니다. 그 동안 이용해 주셔서 감사합니다.')");
        out.println("location.href='" + request.getContextPath() + "/main.do'");
      } else {
        out.println("alert('회원 탈퇴되지 않았습니다.')");
        out.println("history.back()");
      }
      out.println("</script>");
      out.flush();
      out.close();
      
    } catch (Exception e) {
      e.printStackTrace();
    }
    
  }
  
  @Override
  public void inactiveUserBatch() {
    userMapper.insertInactiveUser();
    userMapper.deleteUserForInactive();
  }
  
  @Override
  public void active(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
  
    InactiveUserDto inactiveUser = (InactiveUserDto)session.getAttribute("inactiveUser");
    String email = inactiveUser.getEmail();
    
    int insertActiveUserResult = userMapper.insertActiveUser(email);
    int deleteInactiveUserResult = userMapper.deleteInactiveUser(email);
    
    try {
      response.setContentType("text/html; charset=UTF-8");
      PrintWriter out = response.getWriter();
      out.println("<script>");
      if(insertActiveUserResult == 1 && deleteInactiveUserResult == 1) {
        out.println("alert('휴면계정이 복구되었습니다. 계정 활성화를 위해서 곧바로 로그인 해 주세요.')");
        out.println("location.href='" + request.getContextPath() + "/main.do'");  // 로그인 페이지로 보내면 로그인 후 다시 휴면 계정 복구 페이지로 돌아오므로 main으로 이동한다.
      } else {
        out.println("alert('휴면계정이 복구가 실패했습니다. 다시 시도하세요.')");
        out.println("history.back()");
      }
      out.println("</script>");
      out.flush();
      out.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    
  }

  @Override
  public void findId(HttpServletRequest request, HttpServletResponse response) {
  
    
  }
  //아이디 찾기 
  @Override
  public UserDto find_id(String name, String mobile) {
    UserDto result = null;
    
    try {
     result= userMapper.getFindId(Map.of("name", name, "mobile", mobile));
     
    } catch(Exception e) {
      
      e.printStackTrace();
    }
    
    return result ;
  }
}
  
