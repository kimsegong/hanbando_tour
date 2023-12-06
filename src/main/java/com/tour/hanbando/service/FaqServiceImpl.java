package com.tour.hanbando.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.tour.hanbando.dao.FaqMapper;
import com.tour.hanbando.dao.NoticeMapper;
import com.tour.hanbando.dto.FaqDto;
import com.tour.hanbando.dto.NoticeDto;
import com.tour.hanbando.dto.ReviewDto;
import com.tour.hanbando.util.MyPageUtils;

import lombok.RequiredArgsConstructor;


@Transactional
@RequiredArgsConstructor
@Service
public class FaqServiceImpl implements FaqService {
  private final FaqMapper faqMapper;
  private final MyPageUtils myPageUtils;
  
@Transactional(readOnly=true)  
@Override
public void loadFaqList(HttpServletRequest request, Model model) {
  // TODO Auto-generated method stub
  
  Map<String, Object> map = new HashMap<>();
  
  List<FaqDto> faqList = faqMapper.getFaqList(map);
  
  model.addAttribute("faqList", faqList);
  
}
@Override
public void loadFaqCashList(HttpServletRequest request, Model model) {
  
  Map<String, Object> map = new HashMap<>();
  
  List<FaqDto> faqCashList = faqMapper.getFaqCashList(map);
  
  model.addAttribute("faqCashList", faqCashList);

}

@Override
public void loadFaqKoreaList(HttpServletRequest request, Model model) {
  
  Map<String, Object> map = new HashMap<>();
  
  List<FaqDto> faqKoreaList = faqMapper.getFaqKoreaList(map);
  
  model.addAttribute("faqKoreaList", faqKoreaList);
  

}

@Override
public void loadFaqMemberList(HttpServletRequest request, Model model) {
  
  Map<String, Object> map = new HashMap<>();
  
  List<FaqDto> faqMemberList = faqMapper.getFaqMemberList(map);
  
  model.addAttribute("faqMemberList", faqMemberList);
  

}

@Override
public Map<String, Object> loadOfList(HttpServletRequest request) {
  int faqNo = Integer.parseInt(request.getParameter("faqNo"));
  
  String pageParameter = request.getParameter("page");
  int page = 1;  // 기본값 설정
  if (pageParameter != null && !pageParameter.isEmpty()) {
      try {
          page = Integer.parseInt(pageParameter);
      } catch (NumberFormatException e) {             
          e.printStackTrace();  
      }
  }
  int total = faqMapper.getFaqCount(faqNo);
  int display = 10;
  
  myPageUtils.setPaging(page, total, display);
  
  Map<String, Object> map = Map.of("faqNo", faqNo
                                 , "begin", myPageUtils.getBegin()
                                 , "end", myPageUtils.getEnd());
  
  List<FaqDto> faqList = faqMapper.getFaqList(map);
  String paging = myPageUtils.getAjaxPaging();
  Map<String, Object> result = new HashMap<String, Object>();
  result.put("faqList", faqList);
  result.put("paging", paging);
  return result;
}
}

