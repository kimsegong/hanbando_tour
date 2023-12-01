package com.tour.hanbando.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.tour.hanbando.dto.InquiryDto;

@Mapper
public interface InquiryMapper {
  public List<InquiryDto> getInquiryList(Map<String, Object> map);
  public int insertInquiry(InquiryDto inquiry);
  public InquiryDto getInquiry(int inquiryNo);
}
