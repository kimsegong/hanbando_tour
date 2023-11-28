package com.tour.hanbando.intercept;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.tour.hanbando.dto.UserDto;

/**
 * RequiredAdministratorLoginInterceptor
 * 관리자 로그인이 필요한 기능을 요청할 때 관리자 로그인 여부를 점검하는 인터셉터
 * @author 심희수
 *
 */

@Component
public class RequiredAdministratorLoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        
        if (session != null) {
            UserDto userDto = (UserDto) session.getAttribute("user");
            
            // 세션에 사용자 정보가 없거나 사용자의 권한이 0이 아닌 경우
            if (userDto == null || userDto.getAuth() != 0) {
                response.setContentType("text/html; charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.println("<script>");
                out.println("alert('관리자 로그인이 필요한 기능입니다.');");
                out.println("location.href='" + request.getContextPath() + "/user/login.form';");
                out.println("</script>");
                out.flush();
                out.close();

                return false; // 가로챈 컨트롤러 요청이 더 이상 동작하지 않는다.
            }
        } else {
            // 세션이 없는 경우
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>");
            out.println("alert('관리자 로그인이 필요한 기능입니다.');");
            out.println("location.href='" + request.getContextPath() + "/user/login.form';");
            out.println("</script>");
            out.flush();
            out.close();

            return false; // 가로챈 컨트롤러 요청이 더 이상 동작하지 않는다.
        }

        return true; // 일단 가로챈 컨트롤러 요청이 동작한다.
    }
}
