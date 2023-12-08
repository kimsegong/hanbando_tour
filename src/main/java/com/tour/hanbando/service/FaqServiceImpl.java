package com.tour.hanbando.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import com.tour.hanbando.dao.FaqMapper;
import com.tour.hanbando.dto.FaqCaDto;
import com.tour.hanbando.dto.FaqDto;
import com.tour.hanbando.dto.NoticeDto;
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
public Map<String, Object> loadFaqList(HttpServletRequest request) {
  
  Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
  int page = Integer.parseInt(opt.orElse("1"));
  int total = faqMapper.getFaqCount();
  int display = 10;
  
  myPageUtils.setPaging(page, total, display);
  
  Map<String, Object> map = Map.of("begin", myPageUtils.getBegin()
                                 , "end", myPageUtils.getEnd());
  
  List<FaqDto> faqList = faqMapper.getFaqList(map);
  
  String paging = myPageUtils.getAjaxPaging();
  Map<String, Object> result = new HashMap<String, Object>();
  result.put("faqList", faqList);
  result.put("paging", paging);
  return result;
  
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
  int total = faqMapper.getFaqCount();
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

@Override
public int addFaq(HttpServletRequest request) {
  String title = request.getParameter("title");
  String contents = request.getParameter("contents");
  
  
  FaqDto faq = FaqDto.builder()
                   .caNo(Integer.parseInt(request.getParameter("caNo")))
                   .title(title)
                   .contents(contents)
                   .build();
  
  int addResult = faqMapper.insertFaq(faq);
  
  return addResult;
}

@Override
public int addFaqDetail(HttpServletRequest request) {
     String caTitle = request.getParameter("caTitle");
     
     FaqCaDto faqCaDto = FaqCaDto.builder()
                           .caTitle(caTitle)
                           .build();
     
     int faqCaResult = faqMapper.insertFaqDetail(faqCaDto);
     
     return faqCaResult;
 }

@Transactional(readOnly=true)
@Override
public void getFaqDetail(HttpServletRequest request, Model model) {
  
    String caNo = request.getParameter("caNo");
    
    Map<String, Object> map = new HashMap<>();
    
    map.put("caNo", caNo);

    List<FaqCaDto> faqCaList = faqMapper.getFaqDetail(map);
    
    model.addAttribute("faqCaList", faqCaList);
}

@Override
public Map<String, Object> removeFaq(int FaqNo) {
  int removeResult = faqMapper.deleteFaq(FaqNo);
  return Map.of("removeResult", removeResult);
}

   @Override
   public int modifyFaq(HttpServletRequest request) {
     int faqNo = Integer.parseInt(request.getParameter("faqNo"));
     String title = request.getParameter("title");
     String contents = request.getParameter("contents");
   //수정할 제목/내용/블로그번호를 가진 BlogDto
     FaqDto faq = FaqDto.builder()
                   .faqNo(faqNo)
                     .title(title)
                     .contents(contents)
                     .build();
     
     // BLOG_T 수정
     int modifyResult = faqMapper.updateFaq(faq);
     
     return modifyResult;
   }
   
   public FaqDto getFaq(int faqNo) {
     return faqMapper.getFaq(faqNo);
   }
}