package com.tour.hanbando.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.tour.hanbando.dto.InquiryDto;
import com.tour.hanbando.dto.NoticeDto;

@Mapper
public interface InquiryMapper {
  public List<InquiryDto> getInquiryList();
  public int insertInquiry(InquiryDto inquiry);
  public InquiryDto getInquiry(int inquiryNo);
  public int deleteInquiry(int inquiryNo);
  public int updateInquiry(InquiryDto inquiry);
}
