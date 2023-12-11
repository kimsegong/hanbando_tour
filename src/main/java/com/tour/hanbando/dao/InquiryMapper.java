package com.tour.hanbando.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.tour.hanbando.dto.InquiryAnswerDto;
import com.tour.hanbando.dto.InquiryDto;
import com.tour.hanbando.dto.UserDto;

@Mapper
public interface InquiryMapper {
  
  /*회원 1:1목록*/
  public List<InquiryDto> getUserInquiryList(int userNo);
  public int getUserInquiryCount(int userNo);
  /*관리자 1:1목록*/
  public List<InquiryDto> getInquiryList();
  
  public int insertInquiry(InquiryDto inquiry);
  public InquiryDto getInquiry(int inquiryNo);
  public int deleteInquiry(int inquiryNo);
  public int updateInquiry(InquiryDto inquiry);
  
  public int insertInquiryAnswer(InquiryAnswerDto answer);
  public InquiryAnswerDto getInquiryAnswer(int inquiryNo);

  public int getSearchInquiryListCount(Map<String, Object> map);
  public List<InquiryDto> getSearchInquiyList(Map<String, Object> map);
}
