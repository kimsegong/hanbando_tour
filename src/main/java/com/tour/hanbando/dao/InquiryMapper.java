package com.tour.hanbando.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.tour.hanbando.dto.InquiryAnswerDto;
import com.tour.hanbando.dto.InquiryDto;

@Mapper
public interface InquiryMapper {
  public List<InquiryDto> getInquiryList();
  public int insertInquiry(InquiryDto inquiry);
  public InquiryDto getInquiry(int inquiryNo);
  public int deleteInquiry(int inquiryNo);
  public int updateInquiry(InquiryDto inquiry);
  
  public int insertInquiryAnswer(InquiryAnswerDto answer);
  public InquiryAnswerDto getInquiryAnswer(int inquiryNo);
}
