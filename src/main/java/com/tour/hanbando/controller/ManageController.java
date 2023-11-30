package com.tour.hanbando.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tour.hanbando.dto.InactiveUserDto;
import com.tour.hanbando.dto.UserDto;
import com.tour.hanbando.service.ManageService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/manage")
@RequiredArgsConstructor
@Controller
public class ManageController {
  
  private final ManageService manageService;
  
  /* 기존 회원 목록 */
  @GetMapping("/userList.do")
  public String userList(HttpServletRequest request, Model model) {
    manageService.loadUserList(request, model);
    return "manage/userList";
  }
  
  /* 기존 회원 검색 */
  @GetMapping("/searchUserList.do")
  public String searchUserList(HttpServletRequest request, Model model) {
    manageService.loadSearchUserList(request, model);
    return "manage/userList";
  }
  
  /* 기존 회원 상세 */
  @GetMapping("/userDetail.do")
  public String userDetail(@RequestParam(value="userNo", required=false, defaultValue="0") int userNo
                         , Model model) {
    UserDto user = manageService.getUser(userNo);
    model.addAttribute("user", user);
    return "manage/userDetail";
  }
  
  /* 기존 회원 정보 수정 */
  @PostMapping(value="/modifyUser.do", produces=MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Map<String, Object>> modifyUser(HttpServletRequest request){
    return manageService.modifyUser(request);
  }
  
  /* 기존 회원 비밀번호 수정 폼으로 이동 */
  
  /* 기존 회원 비밀번호 수정하기 */
  
  /* 기존 회원 탈퇴 */
  @PostMapping("/leaveUser.do")
  public String leaveUser(@RequestParam(value="userNo",required=false, defaultValue="0") int userNo
                        , RedirectAttributes redirectAttributes) {
    int leaveUserResult = manageService.leaveUser(userNo);
    redirectAttributes.addFlashAttribute("leaveUserResult", leaveUserResult);
    return "redirect:/manage/leaveUserList.do";
  }
  
  /* 휴면 회원 목록 */
  @GetMapping("/inactiveList.do")
  public String inactiveList(HttpServletRequest request, Model model) {
    manageService.loadInactiveList(request, model);
    return "manage/inactiveUserList";
  }
  
  /* 휴면 회원 검색 */
  @GetMapping("/searchInactiveList.do")
  public String searchInactiveList(HttpServletRequest request, Model model) {
    manageService.loadSearchInactiveList(request, model);
    return "manage/inactiveUserList";
  }
  
  /* 휴면 회원 상세 */
  @GetMapping("/inactiveDetail.do")
  public String inactiveDetail(@RequestParam(value="userNo", required=false, defaultValue="0") int userNo
                             , Model model) {
    InactiveUserDto inactiveUser = manageService.getInactiveUser(userNo);
    model.addAttribute("inactiveUser", inactiveUser);
    return "manage/inactiveDetail";
  }
  
  /* 탈퇴 회원 목록 */
  @GetMapping("/leaveUserList.do")
  public String leaveUserList(HttpServletRequest request, Model model) {
    manageService.loadLeaveUserList(request, model);
    return "manage/leaveUserList";
  }
  
  /* 탈퇴 회원 검색 */
  @GetMapping("/searchLeaveList.do")
  public String searchLeaveList(HttpServletRequest request, Model model) {
    manageService.loadSearchLeaveList(request, model);
    return "manage/leaveUserList";
  }
  
  /* 패키지 상품 목록 */
  @GetMapping("/productList.do")
  public String productList(HttpServletRequest request, Model model) {
    return "manage/productList";
  }
  
  /* 패키지 상품 검색 */
  @GetMapping("/packageProductSearch.do")
  public String packageProductSearch(HttpServletRequest request, Model model) {
    return "manage/productList";
  }
  
  /* 호텔 상품 목록 */
  @GetMapping("/hotelProductList.do")
  public String hotelProductList(HttpServletRequest request, Model model) {
    return "manage/hotelProductList";
  }
  
  /* 호텔 상품 검색 */
  @GetMapping("/hotelProductSearch.do")
  public String hotelProductSearch(HttpServletRequest request, Model model) {
    return "manage/hotelProductList";
  }
  
  /* 패키지 예약 목록 */
  @GetMapping("/reserveList.do")
  public String reserveList(HttpServletRequest request, Model model) {
    return "manage/reserveList";
  }
  
  /* 패키지 예약 검색 */
  
  /* 패키지 예약 상세 */
  
  /* 호텔 예약 목록 */
  
  
  /* 호텔 예약 검색 */
  
  /* 호텔 예약 상세 */
  
  /* 전체 리뷰 목록 */
  @GetMapping("/reviewList.do")
  public String reviewList(HttpServletRequest request, Model model) {
    return "manage/reviewList";
  }
  
  /* 리뷰 검색 */
  @GetMapping("/searchReview.do")
  public String searchReview(HttpServletRequest request, Model model) {
    return "manage/reviewList";
  }
  
  /* 리뷰 삭제 */
  @PostMapping("/removeReview.do")
  public String removeReview(@RequestParam(value="reviewNo") int reviewNo, RedirectAttributes redirectAttributes) {
    return "redirect:/manage/reviewList.do";
  }
  
}