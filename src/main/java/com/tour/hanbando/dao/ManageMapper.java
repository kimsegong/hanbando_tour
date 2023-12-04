package com.tour.hanbando.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.tour.hanbando.dto.HeartDto;
import com.tour.hanbando.dto.InactiveUserDto;
import com.tour.hanbando.dto.LeaveUserDto;
import com.tour.hanbando.dto.ReviewDto;
import com.tour.hanbando.dto.UserDto;

@Mapper
public interface ManageMapper {

  /* 회원 관리 목록 */
  public int getUserCount();
  public List<UserDto> getUserList(Map<String, Object> map);

  /* 회원 검색결과 목록 */
  public int getSearchUserCount(Map<String, Object> map);
  public List<UserDto> getSearchUser(Map<String, Object> map);
  
  /* 찜 목록 */

  /* 휴면회원 관리 목록 */
  public int getInactiveCount();
  public List<InactiveUserDto> getInactiveList(Map<String, Object> map);
  
  /* 휴면회원 검색결과 목록 */
  public int getSearchInactiveCount(Map<String, Object> map);
  public List<InactiveUserDto> getSearchInactive(Map<String, Object> map);
  
  /* 휴면회원 상세 */
  public InactiveUserDto getInactiveUser(int userNo);
  
  /* 탈퇴회원 관리 목록 */
  public int getLeaveUserCount();
  public List<LeaveUserDto> getLeaveUserList(Map<String, Object> map);
  
  /* 탈퇴회원 검색결과 목록 */
  public int getSearchLeaveCount(Map<String, Object> map);
  public List<LeaveUserDto> getSearchLeaveList(Map<String, Object> map);
  
  /* 리뷰 목록 */
  public int getReviewCount();
  public List<ReviewDto> getReviewList(Map<String, Object> map);
  
  /* 리뷰 검색 */
  public int getSearchReviewCount(Map<String, Object> map);
  public List<ReviewDto> getSearchReviewList(Map<String, Object> map);
  
  /* 리뷰 삭제 */
  public int deleteReview(int reviewNo);
  
  
  /*  */
  

}