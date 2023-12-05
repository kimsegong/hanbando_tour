package com.tour.hanbando.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import com.tour.hanbando.dto.InactiveUserDto;
import com.tour.hanbando.dto.UserDto;

public interface ManageService {

    /* 기존 회원 목록 */
    public void loadUserList(HttpServletRequest request, Model model);
    
    /* 기존 회원 검색 */
    public void loadSearchUserList(HttpServletRequest request, Model model);
   
    /* 기존 회원 상세 */
    public UserDto getUser(int userNo);
    
    /* 기존 회원 정보 수정 */
    public ResponseEntity<Map<String, Object>> modifyUser(HttpServletRequest request);
    
    /* 기존 회원 비밀번호 수정 */
    public int modifyPw(HttpServletRequest request);
    
    /* 기존 회원 찜목록 상세 */
    
    /* 기존 회원 탈퇴 */
    public void leaveUser(HttpServletRequest request, HttpServletResponse response);
    
    /* 휴면 회원 목록 */
    public void loadInactiveList(HttpServletRequest request, Model model);
    
    /* 휴면 회원 검색 */
    public void loadSearchInactiveList(HttpServletRequest request, Model model);
    
    /* 휴면 회원 상세 */
    public InactiveUserDto getInactiveUser(int userNo);
    
    /* 탈퇴 회원 목록 */
    public void loadLeaveUserList(HttpServletRequest request, Model model);
    
    /* 탈퇴 회원 검색 */
    public void loadSearchLeaveList(HttpServletRequest request, Model model);
    
    /* 패키지 상품 목록 */
    public void loadPackageList(HttpServletRequest request, Model model);
    
    /* 패키지 상품 검색 */
    
    /* 호텔 상품 목록 */
    public void loadHotelList(HttpServletRequest request, Model model);
    
    
    /* 호텔 객실 가격 변경 */
    public ResponseEntity<Map<String, Object>> modifyRoomPrice(HttpServletRequest request);
    
    /* 호텔 상품 검색 */
    
    /* 패키지 예약 검색 */
    
    /* 패키지 예약 상세 */
    
    /* 호텔 예약 목록 */
    
    /* 호텔 예약 검색 */
    
    /* 호텔 예약 상세 */
    
    /* 패키지 추천 변경 */
    public ResponseEntity<Map<String, Object>> modifyPackageRecommend(HttpServletRequest request);
    
    /* 호텔 추천 변경 */
    public ResponseEntity<Map<String, Object>> modifyHotelRecommend(HttpServletRequest request);
    
    /* 전체 리뷰 목록 */
    public void loadReviewList(HttpServletRequest request, Model model);
    
    /* 리뷰 검색 */
    public void loadSearchReviewList(HttpServletRequest request, Model model);
    
    /* 리뷰 삭제 */
    public int removeReview(int reviewNo);
    
}
