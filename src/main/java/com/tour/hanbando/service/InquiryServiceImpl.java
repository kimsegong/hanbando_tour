package com.tour.hanbando.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.tour.hanbando.dao.InquiryMapper;
import com.tour.hanbando.dto.InquiryAnswerDto;
import com.tour.hanbando.dto.InquiryDto;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class InquiryServiceImpl implements InquiryService {
  private final InquiryMapper inquiryMapper;
  
  @Transactional
  @Override
  public void loadInquiryList(HttpServletRequest request, Model model) {
  
  
  List<InquiryDto> inquiryList = inquiryMapper.getInquiryList();  
  
  model.addAttribute("inquiryList", inquiryList);
  
  
  }
  
  @Override
  public int addInquiry(HttpServletRequest request) {
    String title = request.getParameter("title");
    String contents = request.getParameter("contents");
    int userNo=Integer.parseInt(request.getParameter("userNo"));
    String separate = request.getParameter("separate");
    
    InquiryDto inquiry = InquiryDto.builder()
                     .title(title)
                     .contents(contents)
                     .userNo(userNo)
                     .separate(separate)
                     .build();
    
    int addResult = inquiryMapper.insertInquiry(inquiry);
    
    return addResult;
  }
  
  @Override
  public InquiryDto loadInquiry(int inquiryNo) {
    return inquiryMapper.getInquiry(inquiryNo);
  }
  
  @Override
  public int removeInquiry(int inquiryNo) {
     
    return inquiryMapper.deleteInquiry(inquiryNo);
  }
  
  @Override
  public int modifyInquiry(HttpServletRequest request) {
 // 수정할 제목/내용/블로그번호
    String title = request.getParameter("title");
    String contents = request.getParameter("contents");
    int inquiryNo = Integer.parseInt(request.getParameter("inquiryNo"));
// 수정할 제목/내용/블로그번호를 가진 BlogDto
    InquiryDto inquiry = InquiryDto.builder()
                    .inquiryNo(inquiryNo)
                    .title(title)
                    .contents(contents)
                    .build();
    
    // BLOG_T 수정
    int modifyResult = inquiryMapper.updateInquiry(inquiry);
    
    return modifyResult;
  } 
  
  @Override
  public int addInquiryAnswer(HttpServletRequest request) {
    int inquiryNo=Integer.parseInt(request.getParameter("inquiryNo"));
    String contents = request.getParameter("contents");
    int userNo=Integer.parseInt(request.getParameter("userNo"));
    
    InquiryAnswerDto answer = InquiryAnswerDto.builder()
                             .userNo(userNo)
                             .inquiryNo(inquiryNo)
                             .contents(contents)
                             .build();
    
    int addResult = inquiryMapper.insertInquiryAnswer(answer);
    
    return addResult;
  }
  @Override
  public InquiryAnswerDto loadInquiryAnswer(int inquiryNo) {
    
    return inquiryMapper.getInquiryAnswer(inquiryNo);
  }
}
