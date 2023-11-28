package com.tour.hanbando.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.tour.hanbando.dao.ManageMapper;
import com.tour.hanbando.dto.UserDto;
import com.tour.hanbando.util.MyPageUtils;
import com.tour.hanbando.util.MySecurityUtils;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class ManageServiceImpl implements ManageService {

  private final ManageMapper manageMapper;
  private final MyPageUtils myPageUtils;
  private final MySecurityUtils mySecurityUtils;
  
  
  /**
   * 기존 회원 목록
   * MVC 페이징 처리
   * 
   * @author 심희수
   * @param request
   * @param model
   */
  @Transactional(readOnly=true)
  @Override
  public void loadUserList(HttpServletRequest request, Model model) {

    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    int page = Integer.parseInt(opt.orElse("1"));
    int total = manageMapper.getUserCount();
    int display = 20;
    
    myPageUtils.setPaging(page, total, display);
    
    Map<String, Object> map = Map.of("begin", myPageUtils.getBegin()
                                   , "end", myPageUtils.getEnd());
    
    List<UserDto> userList = manageMapper.getUserList(map);
    
    model.addAttribute("userList", userList);
    model.addAttribute("paging", myPageUtils.getMvcPaging(request.getContextPath() + "/manage/userList.do"));
    model.addAttribute("beginNo", total - (page - 1) * display);
    model.addAttribute("total", total);
    
  }
  
  /**
   * 기존 회원 검색
   * 
   * @author 심희수
   * @param request
   * @param model
   */
  @Transactional(readOnly=true)
  @Override
  public void loadSearchUserList(HttpServletRequest request, Model model) {
    
    String column = request.getParameter("column");
    String query = request.getParameter("query");
    
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("column", column);
    map.put("query", query);
    
    int total = manageMapper.getSearchUserCount(map);
    
    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    String strPage = opt.orElse("1");
    int page = Integer.parseInt(strPage);
    int display = 20;
    
    myPageUtils.setPaging(page, total, display);
    
    map.put("begin", myPageUtils.getBegin());
    map.put("end", myPageUtils.getEnd());
    
    List<UserDto> userList = manageMapper.getSearchUser(map);
    
    model.addAttribute("userList", userList);
    model.addAttribute("paging", myPageUtils.getMvcPaging(request.getContextPath() + "/manage/userSearchList.do", "column=" + column + "&query=" + query));
    model.addAttribute("beginNo", total - (page - 1) * display);
    model.addAttribute("total", total);
    
  }
  
  
}