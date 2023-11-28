package com.tour.hanbando.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.tour.hanbando.dto.ReserveDto;
import com.tour.hanbando.dto.TouristDto;

@Mapper
public interface ReserveMapper {
  
  public int getReserveCount();
  public int getReserveCountByUserNo(int userNo);
  
  public List<ReserveDto> getReserveList(Map<String, Object> map);
  public List<ReserveDto> getReserveListByUser(Map<String, Object> map);
  public ReserveDto getReserve(int reserveNo);
  
  public List<TouristDto> getTourists(int reserveNo);
  
  public int deleteReserve(int reserveNo);
}
