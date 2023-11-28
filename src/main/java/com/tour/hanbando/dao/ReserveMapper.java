package com.tour.hanbando.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.tour.hanbando.dto.ReserveDto;

@Mapper
public interface ReserveMapper {
  
  public int getReserveCount();
  public List<ReserveDto> getReserveList(Map<String, Object> map);
  
}
