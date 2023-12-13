package com.tour.hanbando.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.tour.hanbando.dto.FaqCaDto;
import com.tour.hanbando.dto.FaqDto;

@Mapper
public interface FaqMapper {
  public List<FaqDto> getFaqList(Map<String, Object> map);
  public List<FaqDto> getFaqKoreaList(Map<String, Object> map);
  public List<FaqDto> getFaqCashList(Map<String, Object> map);
  public List<FaqDto> getFaqMemberList(Map<String, Object> map);
  public FaqDto getFaq(int faqNo);
  public int getFaqCount();
  public int insertFaqDetail(FaqCaDto faqCa);
  public List<FaqCaDto> getFaqDetail();
  public int insertFaq(FaqDto faq);
  public int deleteFaq(int FaqNo);
  public int updateFaq(FaqDto faq);
  public FaqCaDto getFaqCa(int faqCaNo);
  public List<FaqCaDto> getFaqCaList();
}