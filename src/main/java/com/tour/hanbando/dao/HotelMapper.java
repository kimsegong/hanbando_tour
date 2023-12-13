package com.tour.hanbando.dao;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.tour.hanbando.dto.FacilitiesDto;
import com.tour.hanbando.dto.HeartDto;
import com.tour.hanbando.dto.HotelDto;
import com.tour.hanbando.dto.HotelImageDto;
import com.tour.hanbando.dto.RegionDto;
import com.tour.hanbando.dto.ReserveDto;
import com.tour.hanbando.dto.ReviewDto;
import com.tour.hanbando.dto.RoomFeatureDto;
import com.tour.hanbando.dto.RoompriceDto;
import com.tour.hanbando.dto.RoomtypeDto;

@Mapper
public interface HotelMapper {
  public List<HotelDto> selectHotelList(Map<String, Object> map);
  public int countHotel(int recommend);
  public List<RoompriceDto> getListPrice(List<HotelDto> hotelDto);
  public int hotelHit(int hotelNo);
  public List<HotelDto> getReviewHotelList(Map<String, Object> map);
  public List<HotelDto> getRecommendHotelList(Map<String, Object> map);
  public List<HotelDto> getPriceHotelList(Map<String, Object> map);
  public List<RegionDto> getRegion();
  
  public int getHotelNo();
  public int insertHotelNo(int HotelNo);
  
  public int insertRoomtype(RoomtypeDto roomtypeDto);
  public int insertRoomFeature(RoomFeatureDto roomFeatureDto);
  public int insertRoomPrice(RoompriceDto roompriceDto);
  public int insertRoomImage(HotelImageDto hotelImageDto);
  
  public int updateHotel(HotelDto hotelDto);
  public int insertFacilities(FacilitiesDto facilitiesDto);
  
  public RoompriceDto getEachRoomPrice(int roomNo);
  
  
  
  public HotelDto getHotel(int hotelNo);
  public List<HotelImageDto> getHotelImage(int hotelNo);
  public List<HotelImageDto> getRoomImage(int roomNo);
  public FacilitiesDto getFacilityies(int hotelNo);
  
  public List<RoomtypeDto> getRoomtype(int hotelNo);
  public List<RoomFeatureDto> getRoomFeature(List<RoomtypeDto> roomtypeDto);
  public RoomFeatureDto getEachRoomFeature(int roomNo);
  public List<RoompriceDto> getPrice(RoomtypeDto roomtypeDto);
  public int deleteRoom(int roomNo);
  
  public RoomtypeDto roomtype(int roomNo);
  public int countReserveRoom(Map<String, Object> map);
  
  public int insertReview(ReviewDto reviewDto);
  public int getReviewCount(int hotelNo);
  public List<ReviewDto> getReviewList(Map<String, Object> map);
  public List<ReserveDto> getReserve(int hotelNo);
  public int deleteReview(int reviewNo);
  
  public int deleteHeart(HeartDto heartDto);
  public int insertHeart(HeartDto heartDto);
  public int getCountHeart(HeartDto heartDto);
  public List<HeartDto> selectHotelHeartList(Map<String, Object> map);
  public int deleteHotelHeart(int hotelNo);
  
  public int deleteHotel(int hotelNo);
  Double starAve(@Param("hotelNo") int hotelNo);
}
