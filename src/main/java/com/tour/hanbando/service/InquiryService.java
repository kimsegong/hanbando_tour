package com.tour.hanbando.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import com.tour.hanbando.dto.InquiryAnswerDto;
import com.tour.hanbando.dto.InquiryDto;

public interface InquiryService {
  
  /*회원 1:1목록*/
  public void loadUserInquiryList(int userNo, Model model);
  
  /*관리자 1:1목록*/
  public void loadInquiryList(HttpServletRequest request, Model model);
  
  public int addInquiry(HttpServletRequest request);
  public InquiryDto loadInquiry(int inquiryNo);
  public int removeInquiry(int inquiryNo);
  public int modifyInquiry(HttpServletRequest request);
  public int addInquiryAnswer(HttpServletRequest request);
  public InquiryAnswerDto loadInquiryAnswer(int inquiryNo);

  public void loadSearchInquiryList(HttpServletRequest request, Model model);
}

