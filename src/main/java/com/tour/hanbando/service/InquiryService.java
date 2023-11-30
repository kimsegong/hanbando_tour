package com.tour.hanbando.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import com.tour.hanbando.dto.InquiryDto;
import com.tour.hanbando.dto.NoticeDto;

public interface InquiryService {
  public void loadInquiryList(HttpServletRequest request, Model model);
  public int addInquiry(HttpServletRequest request, Model model);
  public InquiryDto loadInquiry(int inquiryNo);
}

