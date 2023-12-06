package com.tour.hanbando.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.tour.hanbando.dto.FacilitiesDto;
import com.tour.hanbando.dto.HotelDto;
import com.tour.hanbando.dto.HotelImageDto;
import com.tour.hanbando.dto.RegionDto;
import com.tour.hanbando.dto.RoomFeatureDto;
import com.tour.hanbando.dto.RoompriceDto;
import com.tour.hanbando.dto.RoomtypeDto;

@Mapper
public interface HotelMapper {
  public List<HotelDto> selectHotelList(Map<String, Object> map);
  public int countHotel();
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
  
  public HotelDto getHotel(int hotelNo);
  public List<HotelImageDto> getHotelImage(int hotelNo);
  
  
  public List<RoomtypeDto> getRoomtype(int hotelNo);
  public List<HotelImageDto> getRoomImage(List<RoomtypeDto> roomtypeDto);
  public List<RoomFeatureDto> getRoomFeature(List<RoomtypeDto> roomtypeDto);
  public List<RoompriceDto> getPrice(List<RoomtypeDto> roomtypeDto);
}
