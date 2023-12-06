package com.tour.hanbando.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.tour.hanbando.dto.FaqDto;
import com.tour.hanbando.dto.ReviewDto;

@Mapper
public interface FaqMapper {
  public List<FaqDto> getFaqList(Map<String, Object> map);
  public List<FaqDto> getFaqKoreaList(Map<String, Object> map);
  public List<FaqDto> getFaqCashList(Map<String, Object> map);
  public List<FaqDto> getFaqMemberList(Map<String, Object> map);
  public int getFaqCount(int faqNo);

}
