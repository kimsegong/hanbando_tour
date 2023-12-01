package com.tour.hanbando.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import com.tour.hanbando.dto.InquiryDto;

public interface InquiryService {
  public void loadInquiryList(HttpServletRequest request, Model model);
  public int addInquiry(HttpServletRequest request);
  public InquiryDto loadInquiry(int inquiryNo);
}

