package com.tour.hanbando.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.tour.hanbando.dao.InquiryMapper;
import com.tour.hanbando.dto.InquiryDto;
import com.tour.hanbando.dto.NoticeDto;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class InquiryServiceImpl implements InquiryService {
  private final InquiryMapper inquiryMapper;
  
  @Transactional
  @Override
  public void loadInquiryList(HttpServletRequest request, Model model) {
  
  Map<String, Object> map = new HashMap<>();  
  
  map.put("title", request.getParameter("title")); 
  
  List<InquiryDto> inquiryList = inquiryMapper.getInquiryList(map);  
  
  model.addAttribute("inquiryList", inquiryList);
  
  }
  
  @Override
  public int addInquiry(HttpServletRequest request, Model model) {
    String title = request.getParameter("title");
    String contents = request.getParameter("contents");
    
    InquiryDto inquire = InquiryDto.builder()
                     .title(title)
                     .contents(contents)
                     .build();
    
    int addResult = inquiryMapper.insertInquiry(inquire);
    
    return addResult;
  }
  
  @Override
  public InquiryDto loadInquiry(int inquiryNo) {
    return inquiryMapper.getInquiry(inquiryNo);
  }
}
