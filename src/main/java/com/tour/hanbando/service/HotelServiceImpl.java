package com.tour.hanbando.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

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
              
    
    List<Integer> hPrice = new ArrayList<>();
    List<HotelDto> hotelDto = new ArrayList<>();
    
    int btnVal = Integer.parseInt(request.getParameter("btnVal"));
    
    switch (btnVal) {
    case 0 : hotelDto = hotelMapper.selectHotelList(map);
      break;
    case 1 : hotelDto = hotelMapper.getRecommendHotelList(map);
      break;
    case 2 : hotelDto = hotelMapper.getReviewHotelList(map);
      break;
    case 3 : 
      
      break;
    case 4 :
      
      break;
    }
    System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%" + hotelDto);
    
    if(hotelDto.size() != 0) {
     
      List<RoompriceDto> roompriceDto = hotelMapper.getListPrice(hotelDto);
      
      /* 요금 구하기 */
      Date date = new Date();
      SimpleDateFormat sdf = new SimpleDateFormat("MMdd");
      String sToday = sdf.format(date);
      int today = Integer.parseInt(sToday);
      int price = 0;
      
      for(int i = 0; i < hotelDto.size(); i++) {
        int biStart = Integer.parseInt(roompriceDto.get(i).getBsDate().replace("/", ""));
        int biEnd = Integer.parseInt(roompriceDto.get(i).getBeDate().replace("/", ""));
        
        int jsStart = Integer.parseInt(roompriceDto.get(i).getJsDate().replace("/", ""));
        int jsEnd = Integer.parseInt(roompriceDto.get(i).getJeDate().replace("/", ""));
        
        int ssStart = Integer.parseInt(roompriceDto.get(i).getSsDate().replace("/", ""));
        int ssEnd = Integer.parseInt(roompriceDto.get(i).getSeDate().replace("/", ""));
        
        if(biStart > biEnd ) {
          biEnd += 1200;
        }else if(jsStart > jsEnd){
          jsEnd += 1200;
        }else if(ssStart > ssEnd) {
          ssEnd += 1200;
        }
        
        if(biStart <= today && today <= biEnd) {
         price = roompriceDto.get(i).getBiPrice();
        }else if(jsStart <= today && today <= jsEnd) {
         price = roompriceDto.get(i).getJunPrice();
        }else if(ssStart <= today && today <= ssEnd) {
         price = roompriceDto.get(i).getSungPrice();
        } else {
          price = roompriceDto.get(i).getBiPrice();
        }
        
        
        hPrice.add(price);
      }
    } else {
      hPrice.clear();
    }
    Collections.reverse(hPrice);
    
    Map<String, Object> hotel = Map.of("hotelList", hotelDto
                                      ,"price", hPrice
                                      ,"count", hotelMapper.countHotel()
                                      ,"totalPage", myPageUtils.getTotalPage());
    
    return hotel;
  }

  @Override
  public Map<String, Object> getSortedHotelList(HttpServletRequest request) {
    
    return null;
  }  
  
  @Override
  public int increseHit(int hotelNo) {
    return hotelMapper.hotelHit(hotelNo);
  }
}
