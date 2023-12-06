package com.tour.hanbando.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.tour.hanbando.dto.HotelDto;
import com.tour.hanbando.dto.InactiveUserDto;
import com.tour.hanbando.dto.LeaveUserDto;
import com.tour.hanbando.dto.PackageDto;
import com.tour.hanbando.dto.RegionDto;
import com.tour.hanbando.dto.ReserveDto;
import com.tour.hanbando.dto.ReviewDto;
import com.tour.hanbando.dto.RoompriceDto;
import com.tour.hanbando.dto.RoomtypeDto;
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
  
  /* 패키지 상품 목록 */
  public int getPackageCount();
  public List<PackageDto> getPackageList(Map<String, Object> map);
  
  /* 패키지 상품 검색 */
  public int getSearchPackageProducCount(Map<String, Object> map);
  public List<PackageDto> getSearchPackageProductList(Map<String, Object> map);
    
  /* 호텔 상품 목록 */
  public int getHotelCount();
  public List<HotelDto> getHotelList(Map<String, Object> map);
  
  /* 호텔 상품 검색 */
  public int getSearchHotelProductCount(Map<String, Object> map);
  public List<HotelDto> getSearchHotelProductList(Map<String, Object> map);
  
  /* 호텔 객실별 가격 */
  public List<RoompriceDto> getRoomPrice();
  /* 호텔 객실 타입 */
  public List<RoomtypeDto> getRoomType();
  
  /* 호텔 객실 가격 변경 */
  public int updateRoomPrice(RoompriceDto roomprice);

  /* 지역 목록 */
  public List<RegionDto> getRegionList();
  
  /* 패키지 추천 변경 */
  public int updatePackageRecommend(PackageDto packageDto);
  
  /* 호텔 추천 변경 */
  public int updateHotelRecommend(HotelDto hotel);
  
  /* 예약 목록 */
  public int getReserveCount();
  public List<ReserveDto> getReserveList(Map<String, Object> map);
  
  /* 예약 검색 */
  public int getSearchReserveCount(Map<String, Object> map);
  public List<ReserveDto> getSearchReserveList(Map<String, Object> map);
  
  /* 예약 상세 */
  
  /* 리뷰 목록 */
  public int getReviewCount();
  public List<ReviewDto> getReviewList(Map<String, Object> map);
  
  /* 리뷰 검색 */
  public int getSearchReviewCount(Map<String, Object> map);
  public List<ReviewDto> getSearchReviewList(Map<String, Object> map);
  
  /* 리뷰 삭제 */
  public int deleteReview(int reviewNo);
  
  
}