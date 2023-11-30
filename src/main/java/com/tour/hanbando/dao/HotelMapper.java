package com.tour.hanbando.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.tour.hanbando.dto.HotelDto;

@Mapper
public interface HotelMapper {
  public List<HotelDto> selectHotelList(Map<String, Object> map);
  public int countHotel();

}
