package com.tour.hanbando.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.tour.hanbando.dao.InquiryMapper;
import com.tour.hanbando.dto.InquiryAnswerDto;
import com.tour.hanbando.dto.InquiryDto;
import com.tour.hanbando.dto.UserDto;
import com.tour.hanbando.util.MyPageUtils;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class InquiryServiceImpl implements InquiryService {
  private final InquiryMapper inquiryMapper;
  private final MyPageUtils myPageUtils;
  
  /**
   * 회원 1:1목록
   */
  @Transactional(readOnly = true)
  @Override
  public void loadUserInquiryList(int userNo, Model model) {
    List<InquiryDto> inquiryList = inquiryMapper.getUserInquiryList(userNo);
    int inquiryCount = inquiryMapper.getUserInquiryCount(userNo);
    model.addAttribute("inquiryList", inquiryList);
    model.addAttribute("inquiryCount", inquiryCount);
  }
  
  /**
   * 관리자 1:1목록
   */
  @Transactional(readOnly = true)
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
                     .userDto(UserDto.builder()
                               .userNo(userNo)
                               .build())
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
  
  @Override
  public void loadSearchInquiryList(HttpServletRequest request, Model model) {
    String column = request.getParameter("column");
    String query = request.getParameter("query");
    
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("column", column);
    map.put("query", query);
    
    int total = inquiryMapper.getSearchInquiryListCount(map);
    
    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    String strPage = opt.orElse("1");
    int page = Integer.parseInt(strPage);
    int display = 20;
    
    myPageUtils.setPaging(page, total, display);
    
    map.put("begin", myPageUtils.getBegin());
    map.put("end", myPageUtils.getEnd());
    
    List<InquiryDto> inquiryManageList = inquiryMapper.getSearchInquiyList(map);
    
    model.addAttribute("inquiryManageList", inquiryManageList);
    model.addAttribute("paging", myPageUtils.getMvcPaging(request.getContextPath() + "/notice/inquirySearchList.do", "column=" + column + "&query=" + query));
    model.addAttribute("beginNo", total - (page - 1) * display);
    model.addAttribute("total", total);
    
    
  }
}
