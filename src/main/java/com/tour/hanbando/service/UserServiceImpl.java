package com.tour.hanbando.service;

import java.io.BufferedReader;
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

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tour.hanbando.dao.UserMapper;
import com.tour.hanbando.dto.InactiveUserDto;
import com.tour.hanbando.dto.UserDto;
import com.tour.hanbando.util.MyJavaMailUtils;
import com.tour.hanbando.util.MyPageUtils;
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
  private final String kakao_id = "9ac35c110f888ef0213ea4dbd3fab619";
  private final String kakao_secret = "oSTQjBnyEhgcUN8Xioh6Y5ZiRfKYRe0m";
  
  
  
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
  
    
   //1.비밀번호 90일 변경(1.최근변경날짜를 꺼낸다)
   
    
    
    
    
    
  
  
  @Override
  public void login(HttpServletRequest request, HttpServletResponse response) throws Exception {
    
    String email = request.getParameter("email");
    String pw = mySecurityUtils.getSHA256(request.getParameter("pw"));
    
    Map<String, Object> map = Map.of("email", email
                                   , "pw", pw);

    HttpSession session = request.getSession();
    
   
    
    // 정상적인 로그인 처리하기
    UserDto user = userMapper.getUser(map);
    //int checkDayOfPwModifiedAt = userMapper.recentpWChange(map); // 여기에 경과일수 int 타입으로 반환
    
    if(user != null) {
      session.setAttribute("user", user);
      userMapper.insertAccess(email);
      response.sendRedirect(request.getParameter("referer"));
    
      
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
  
  ////////////////네이버/////////////////////////
  @Override
  public String getNaverLoginURL(HttpServletRequest request) throws Exception {
    
    // 네이버로그인-1
    // 네이버 로그인 연동 URL 생성하기를 위해 redirect_uri(URLEncoder), state(SecureRandom) 값의 전달이 필요하다.
    // redirect_uri : 네이버로그인-2를 처리할 서버 경로를 작성한다.
    // redirect_uri 값은 네이버 로그인 Callback URL에도 동일하게 등록해야 한다.
    
    String apiURL = "https://nid.naver.com/oauth2.0/authorize";
    String response_type = "code";
    String redirect_uri = URLEncoder.encode("http://localhost:8080" + request.getContextPath() + "/user/naver/getAccessToken.do", "UTF-8");
    String state = new BigInteger(130, new SecureRandom()).toString();
  
    StringBuilder sb = new StringBuilder();
    sb.append(apiURL);
    sb.append("?response_type=").append(response_type);
    sb.append("&client_id=").append(client_id);
    sb.append("&redirect_uri=").append(redirect_uri);
    sb.append("&state=").append(state);
    
    return sb.toString();
    
  }
  
  @Override
  public String getNaverLoginAccessToken(HttpServletRequest request) throws Exception {
    
    // 네이버로그인-2
    // 접근 토큰 발급 요청
    // 네이버로그인-2를 수행하기 위해서는 네이버로그인-1의 응답 결과인 code와 state가 필요하다.
    
    // 네이버로그인-1의 응답 결과(access_token을 얻기 위해 요청 파라미터로 사용해야 함)
    String code = request.getParameter("code");
    String state = request.getParameter("state");
    
    String apiURL = "https://nid.naver.com/oauth2.0/token";
    String grant_type = "authorization_code";  // access_token 발급 받을 때 사용하는 값(갱신이나 삭제시에는 다른 값을 사용함)
    
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
  
  @Override
  public UserDto getNaverProfile(String accessToken) throws Exception {
    
    // 네이버 로그인-3
    // 접근 토큰을 전달한 뒤 사용자의 프로필 정보(이름, 이메일, 성별, 휴대전화번호) 받아오기
    // 요청 헤더에 Authorization: Bearer accessToken 정보를 저장하고 요청함
    
    // 요청
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
  public UserDto getUser(String email) {
    return userMapper.getUser(Map.of("email", email));
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
      if(naverJoinResult == 1) {
        request.getSession().setAttribute("user", userMapper.getUser(Map.of("email", email)));
        userMapper.insertAccess(email);
        out.println("alert('네이버 간편가입이 완료되었습니다.')");
      } else {
        out.println("alert('네이버 간편가입이 실패했습니다.')");
      }
      out.println("location.href='" + request.getContextPath() + "/main.do'");
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
  
  ///////////////////////////////////카카오////////////////////////////////////////////////////
  @Override
  public void kakaoJoin(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String mobile = request.getParameter("mobile");
    String email = request.getParameter("email");
    String pw = request.getParameter("pw");
    String name = request.getParameter("name");
    String gender = request.getParameter("gender");
    
    UserDto user = UserDto.builder()    
                          .mobile(mobile)
                          .email(email)
                          .pw(pw)
                          .name(name)
                          .gender(gender)
                          .build();
    
    int kakaoJoinResult = userMapper.insertKakaoUser(user);
    
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
  
  
  @Override
  public String getKakaoLoginURL(HttpServletRequest request) throws Exception {
    // 카카오로그인-1
    // 카카오 로그인 연동 URL 생성하기를 위해 redirect_uri(URLEncoder), state(SecureRandom) 값의 전달이 필요하다.
    // redirect_uri : 카카오로그인-2를 처리할 서버 경로를 작성한다.
    // redirect_uri 값은 카카오 로그인 Callback URL에도 동일하게 등록해야 한다.
    
    String apiURL = "https://kauth.kakao.com/oauth/authorize";
    String response_type = "code";
    String redirect_uri = URLEncoder.encode("http://localhost:8080" + request.getContextPath() + "/user/kakao/getAccessToken.do", "UTF-8");
    String state = new BigInteger(130, new SecureRandom()).toString();
  
    StringBuilder sb = new StringBuilder();
    sb.append(apiURL);
    sb.append("?response_type=").append(response_type);
    sb.append("&client_id=").append(kakao_id);
    sb.append("&redirect_uri=").append(redirect_uri);
    sb.append("&state=").append(state);
    
    return sb.toString();
    
  }
  
  //카카오로그인-2(토큰 발급 요청)
  @Override
  public String getKakaoLoginAccessToken(HttpServletRequest request) throws Exception {
    
    String code = request.getParameter("code");
    String state = request.getParameter("state");
    
    String apiURL = "https://kauth.kakao.com/oauth/token";
    String grant_type = "authorization_code";

    StringBuilder sb = new StringBuilder();
    sb.append(apiURL);
    sb.append("?grant_type=").append(grant_type);
    sb.append("&client_id=").append(kakao_id);
    sb.append("&client_secret=").append(kakao_secret);
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
  
  
  @Override
  public UserDto getKakaoProfile(String accessToken) throws Exception {
    
    // 카카오 로그인-3
    // 접근 토큰을 전달한 뒤 사용자의 프로필 정보(이름, 이메일, 성별, 휴대전화번호) 받아오기
    // 요청 헤더에 Authorization: Bearer accessToken 정보를 저장하고 요청함
    
    // 요청
    String apiURL = "https://kapi.kakao.com/v2/user/me";
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
    
   
    
   
    JSONObject kakao_account = obj.getJSONObject("kakao_account");
    JSONObject profile = kakao_account.getJSONObject("profile");
    
    String nickname = profile.getString("nickname");
    String email = kakao_account.getString("email");
    
    
    UserDto user = UserDto.builder()    
                          .email(email)
                          .name(nickname)
                          .build();
    
   
    
    return user;
    
  }
  
    
    
    
    
  @Override
  public void kakaoLogin(HttpServletRequest request, HttpServletResponse response, UserDto kakaoProfile)
      throws Exception {
    String email = kakaoProfile.getEmail();
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
  
