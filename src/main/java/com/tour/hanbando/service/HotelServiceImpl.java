package com.tour.hanbando.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tour.hanbando.dao.HotelMapper;
import com.tour.hanbando.dto.PackageDto;
import com.tour.hanbando.util.MyPageUtils;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class HotelServiceImpl implements HotelService {
  private final HotelMapper hotelMapper;
  private final MyPageUtils myPageUtils;
  
  
  @Override
  public Map<String, Object> getHotelList(HttpServletRequest request) {
    
    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    int page = Integer.parseInt(opt.orElse("1"));
    int total = hotelMapper.countHotel();
    int display = 9;
    
    myPageUtils.setPaging(page, total, display);
    
    Map<String, Object> map = Map.of("begin", myPageUtils.getBegin()
                                   , "end", myPageUtils.getEnd());
              
    
    Map<String, Object> hotel = Map.of("hotelList", hotelMapper.selectHotelList(map)
                                      ,"count", hotelMapper.countHotel()
                                      ,"totalPage", myPageUtils.getTotalPage());
     
    
    
    
    return hotel;
  }
  
}
