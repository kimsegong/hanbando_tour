package com.tour.hanbando.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.tour.hanbando.dao.NoticeMapper;
import com.tour.hanbando.dto.NoticeDto;
import com.tour.hanbando.util.MyPageUtils;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class NoticeServiceImpl implements NoticeService {
  private final NoticeMapper noticeMapper;
  private final MyPageUtils myPageUtils;
  
  @Transactional(readOnly=true)
  @Override
  public void loadNoticeList(HttpServletRequest request, Model model) {
   
    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    int page = Integer.parseInt(opt.orElse("1"));
    int total = noticeMapper.getNoticeCount();
    int display = 10;
    
    myPageUtils.setPaging(page, total, display);
    
    Map<String, Object> map = Map.of("begin", myPageUtils.getBegin()
                                   , "end", myPageUtils.getEnd());
    
    List<NoticeDto> noticeList = noticeMapper.getNoticeList(map);
    
    model.addAttribute("noticeList", noticeList);
    model.addAttribute("paging", myPageUtils.getMvcPaging(request.getContextPath() + "/notice/list.do"));
    model.addAttribute("beginNo", total - (page - 1) * display);  
    }
  
  
  
  @Override
  public int addNotice(HttpServletRequest request) {
    String title = request.getParameter("title");
    String contents = request.getParameter("contents");
    
    NoticeDto notice = NoticeDto.builder()
                     .title(title)
                     .contents(contents)
                     .build();
    
    int addResult = noticeMapper.insertNotice(notice);
    
    return addResult;
  }
  
  @Transactional(readOnly=true)
  @Override
  public void LoadSearchList(HttpServletRequest request, Model model) {
     
    String column = request.getParameter("column");
    String query = request.getParameter("query");
    
    Map<String, Object> map = new HashMap<>();
    map.put("column", column);
    map.put("query", query);
    
    int total = noticeMapper.getSearchCount(map);
    
    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    String strPage = opt.orElse("1");
    int page = Integer.parseInt(strPage);
    
    int display = 10;
    
    myPageUtils.setPaging(page, total, display);
    
    map.put("begin", myPageUtils.getBegin());
    map.put("end", myPageUtils.getEnd());
    
    List<NoticeDto> NoticeList = noticeMapper.getSearchList(map);
    
    model.addAttribute("noticeList", NoticeList);
    model.addAttribute("paging", myPageUtils.getMvcPaging(request.getContextPath() + "/notice/search.do", "column=" + column + "&query=" + query));
    model.addAttribute("beginNo", total - (page - 1) * display);
  }
  
  @Override
  public NoticeDto loadNotice(int noticeNo) {
    return noticeMapper.getNotice(noticeNo);
  }
  
  @Override
  public int modifyNotice(HttpServletRequest request) {
    String title = request.getParameter("title");
    String contents = request.getParameter("contents");
    int noticeNo = Integer.parseInt(request.getParameter("noticeNo"));
// 수정할 제목/내용/블로그번호를 가진 BlogDto
    NoticeDto notice = NoticeDto.builder()
                    .noticeNo(noticeNo)
                    .title(title)
                    .contents(contents)
                    .build();
    
    // BLOG_T 수정
    int modifyResult = noticeMapper.updateNotice(notice);
    
    return modifyResult;
    
  }
  
  @Override
  public int removeNotice(int NoticeNo) {
    
    return noticeMapper.deleteNotice(NoticeNo);
  }
}
