package com.tour.hanbando.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.tour.hanbando.dao.ReserveMapper;
import com.tour.hanbando.dto.ReserveDto;
import com.tour.hanbando.dto.TouristDto;
import com.tour.hanbando.util.MyPageUtils;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class ReserveServiceImpl implements ReserveService {

  private final ReserveMapper reserveMapper;
  private final MyPageUtils myPageUtils;
  
  @Override
  public void loadReserveList(HttpServletRequest request, Model model) {
    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    int page = Integer.parseInt(opt.orElse("1"));
    int total = reserveMapper.getReserveCount();
    int display = 10;
    myPageUtils.setPaging(page, total, display);
    
    Map<String, Object> map = Map.of("begin", myPageUtils.getBegin()
                                   , "end", myPageUtils.getEnd());
    
    List<ReserveDto> reserveList = reserveMapper.getReserveList(map);
    
    model.addAttribute("reserveList", reserveList);
    model.addAttribute("paging", myPageUtils.getMvcPaging(request.getContextPath() + "/reserve/reserveList.do"));
    model.addAttribute("beginNo", total - (page - 1) * display);
  }

  @Transactional(readOnly=true)
  @Override
  public void loadReserveListByUser(HttpServletRequest request, Model model) {
    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    int page = Integer.parseInt(opt.orElse("1"));
    int display = 10;
    int userNo = Integer.parseInt(request.getParameter("userNo"));
    int total = reserveMapper.getReserveCountByUserNo(userNo);
    
    myPageUtils.setPaging(page, total, display);
    
    Map<String, Object> map = Map.of("begin", myPageUtils.getBegin()
                                   , "end", myPageUtils.getEnd() 
                                   , "userNo", userNo);
    
    List<ReserveDto> reserveList = reserveMapper.getReserveListByUser(map);
    
    model.addAttribute("reserveList", reserveList);
    model.addAttribute("paging", myPageUtils.getMvcPaging(request.getContextPath() + "/reserve/reserveList.do", request.getParameter("userNo")));
    model.addAttribute("beginNo", total - (page - 1) * display);
    
  }
  
  @Override
  public ReserveDto loadReserve(int reserveNo) {
    return reserveMapper.getReserve(reserveNo);
  }
  
  @Transactional(readOnly=true)
  @Override
  public Map<String, Object> loadTourists(HttpServletRequest request) {
    int reserveNo = Integer.parseInt(request.getParameter("reserveNo"));
    List<TouristDto> tourists = reserveMapper.getTourists(reserveNo);
    return Map.of("tourists", tourists);
  }
  
  
  
  
  @Override
  public int removeReserve(HttpServletRequest request) {
    int reserveNo = Integer.parseInt(request.getParameter("reserveNo"));
    int removeResult = reserveMapper.deleteReserve(reserveNo);
    return removeResult;
  }
  
  
  
}
