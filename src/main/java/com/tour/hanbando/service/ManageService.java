package com.tour.hanbando.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
    
    /* 기존 회원 탈퇴 */
    
    /* 휴면 회원 목록 */
    public void loadInactiveList(HttpServletRequest request, Model model);
    
    /* 휴면 회원 검색 */
    public void loadSearchInactiveList(HttpServletRequest request, Model model);
    
    /* 휴면 회원 상세 */
    public InactiveUserDto getInactiveUser(int userNo);
    
    /* 탈퇴 회원 목록 */
    
    /* 탈퇴 회원 검색 */
    
    /* 패키지 상품 목록 */
    
    /* 패키지 상품 검색 */
    
    /* 호텔 상품 목록 */
    
    /* 호텔 상품 검색 */
    
    /* 패키지 예약 검색 */
    
    /* 패키지 예약 상세 */
    
    /* 호텔 예약 목록 */
    
    /* 호텔 예약 검색 */
    
    /* 호텔 예약 상세 */
    
    /* 전체 리뷰 목록 */
    
    /* 리뷰 검색 */
    
    /* 리뷰 삭제 */
    
}
