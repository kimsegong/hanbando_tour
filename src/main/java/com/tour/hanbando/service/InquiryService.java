package com.tour.hanbando.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import com.tour.hanbando.dto.InquiryAnswerDto;
import com.tour.hanbando.dto.InquiryDto;
import com.tour.hanbando.dto.NoticeDto;

public interface InquiryService {
  public void loadInquiryList(HttpServletRequest request, Model model);
  public int addInquiry(HttpServletRequest request);
  public InquiryDto loadInquiry(int inquiryNo);
  public int removeInquiry(int inquiryNo);
  public int modifyInquiry(HttpServletRequest request);
  public int addInquiryAnswer(HttpServletRequest request);
  public InquiryAnswerDto loadInquiryAnswer(int inquiryNo);
}

