package com.tour.hanbando.intercept;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


/**
 * LoginStatusCheckInterceptor
 * 로그인이 필요한 기능을 요청할 때 로그인 여부를 점검하는 인터셉터
 */
@Component
public class RequiredLoginInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

    HttpSession session = request.getSession();
    
    if(session != null && session.getAttribute("user") == null) {
      response.setContentType("text/html; charset=UTF-8");
      PrintWriter out = response.getWriter();
      out.println("<script>");
      out.println("if(confirm('로그인이 필요한 기능입니다. 로그인할까요?')){");
      out.println("location.href='"+ request.getContextPath() +"/user/login.form'");
      out.println("} else {");
      out.println("history.back()");
      out.println("}");
      out.println("</script>");
      out.flush();
      out.close();
      return false; // 가로챈 컨트롤러 요청이 동작하지 않는다.
    }
    return true; // 가로챈 컨트롤러 요청이 동작한다.
  }
  
}
