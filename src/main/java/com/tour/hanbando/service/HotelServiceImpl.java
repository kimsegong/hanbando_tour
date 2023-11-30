package com.tour.hanbando.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.xml.crypto.Data;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tour.hanbando.dao.HotelMapper;
import com.tour.hanbando.dto.HotelDto;
import com.tour.hanbando.dto.RoompriceDto;
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
    
    int end = myPageUtils.getEnd();
    int begin = myPageUtils.getBegin();
    
    
    Map<String, Object> map = Map.of("begin", begin
                                   , "end", end);
              
    List<HotelDto> hotelDto = hotelMapper.selectHotelList(map);
    List<RoompriceDto> roompriceDto = hotelMapper.getListPrice(hotelDto);
    
    Date date = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
    Date today = sdf.(date);
    
    System.out.println(today);
    
    for(int i = 0; i < (end - begin) + 1; i++) {
      Date biStart = sdf.parse(roompriceDto.get(i).getBsDate());
      Date biEnd = sdf.parse(roompriceDto.get(i).getBeDate());
      
      Date jsStart = sdf.parse(roompriceDto.get(i).getJsDate());
      Date jeEnd = sdf.parse(roompriceDto.get(i).getJeDate());
      
      Date ssStart = sdf.parse(roompriceDto.get(i).getSsDate());
      Date seEnd = sdf.parse(roompriceDto.get(i).getSeDate());
      
      if(( && )|| || ) {
      int price = roompriceDto.get(i).getBiPrice();
      
      
      
      
     }
    
    
    }
    
    
    
    
    
    Map<String, Object> hotel = Map.of("hotelList", hotelDto
                                      ,"count", hotelMapper.countHotel()
                                      ,"price", roompriceDto 
                                      ,"totalPage", myPageUtils.getTotalPage());
     
    
    
    return hotel;
  }
  
}
