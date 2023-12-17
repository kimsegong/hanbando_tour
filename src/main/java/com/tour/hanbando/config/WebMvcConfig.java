package com.tour.hanbando.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.tour.hanbando.intercept.RequiredAdministratorLoginInterceptor;
import com.tour.hanbando.intercept.RequiredLoginInterceptor;
import com.tour.hanbando.intercept.ShouldNotLoginInterceptor;

import lombok.RequiredArgsConstructor;

@EnableWebMvc
@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  private final RequiredLoginInterceptor requiredLoginInterceptor;
  private final ShouldNotLoginInterceptor shouldNotLoginInterceptor;
  private final RequiredAdministratorLoginInterceptor administratorInterceptor;
  
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    // 관리자권한이 필요한 접근을 막는 인터셉터
    registry.addInterceptor(administratorInterceptor)
      .addPathPatterns("/manage/userList.do", "/manage/inactiveUserList.do", "/manage/leaveUserList.do")
      .addPathPatterns("/manage/userSearchList.do", "/manage/leaveUserSearchList.do", "/manage/inactiveUserSearchList.do")
      .addPathPatterns("/manage/userDetail.do", "/manage/inactiveDetail.do", "/manage/modifyPw.form", "/manage/heartList.do", "/manage/searchHeartList.do")
      .addPathPatterns("/manage/productList.do", "/manage/hotelProductList.do")
      .addPathPatterns("/manage/packageProductSearch.do", "/manage/hotelProductSearch.do")
      .addPathPatterns("/manage/reserveList.do", "/manage/searchReserve.do")
      .addPathPatterns("/manage/reservePackageDetail.do", "/manage/reserveHotelDetail.do")
      .addPathPatterns("/manage/reviewList.do", "/manage/searchReview.do")
      .addPathPatterns("/notice/write.form")
      .addPathPatterns("/package/write.form", "/package/edit.do");
    
    // 로그인이 필요한 접근을 막는 인터셉터
    registry.addInterceptor(requiredLoginInterceptor)
      .addPathPatterns("/user/mypage.form", "/user/modifyPw.form")
      .addPathPatterns("/user/findpwCheck.do")
      .addPathPatterns("/user/changePw.form")
      .addPathPatterns("/user/heart.do")
      .addPathPatterns("/user/heartHotel.do")
      .addPathPatterns("/notice/inquirywrite.form")
      .addPathPatterns("/reserve/reserveList.do", "/reserve/detail.do", "/reserve/write.form", "/reserve/edit.form")
      .addPathPatterns("/reserve/reserveHotelList.do", "/reserve/writeHotel.form", "/reserve/writeHotel.form", "/reserve/detailHotel.do");
    
    // 로그인이 되어있는 상태에서의 접근을 막는 인터셉터
    registry.addInterceptor(shouldNotLoginInterceptor)
      .addPathPatterns("/user/agree.form", "/user/join.form")
      .addPathPatterns("/user/login.form", "/user/naver/getAccessToken.do","/user/naver/getProfile.do","/user/kakao/getAccessToken.do","/user/kakao/getProfile.do")
      .addPathPatterns("/user/checkEmail.do","/user/sendCode.do")
      .addPathPatterns("/user/active.form","/user/active.do")
      .addPathPatterns("/user/find.form","/user/findPwModified.form","/user/pwCorrect.form");
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/**")
      .addResourceLocations("classpath:/static/", "classpath:/templates/");
    registry.addResourceHandler("/hanbando/mainBanner/**")
      .addResourceLocations("file:/hanbando/mainBanner/");
    registry.addResourceHandler("/hanbando/package/**")
    .addResourceLocations("file:/hanbando/package/");
    registry.addResourceHandler("/hanbando/hotel/**")
    .addResourceLocations("file:/hanbando/hotel/");
    registry.addResourceHandler("/hanbando/notice/**")
    .addResourceLocations("file:/hanbando/notice/");
  }
  
  
  
}
