package com.tour.hanbando.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.tour.hanbando.dto.FaqDto;

@Mapper
public interface FaqMapper {
  public List<FaqDto> getFaqList(Map<String, Object> map);
}
