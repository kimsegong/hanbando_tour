package com.tour.hanbando.dao;

import org.apache.ibatis.annotations.Mapper;

import com.tour.hanbando.dto.InquiryAnswerDto;

@Mapper
public interface InquiryAnswerMapper {

  public int insertInquiryAnswer(InquiryAnswerDto answer);
  public InquiryAnswerDto getInquiryAnswer(int inquiryNo);
}
