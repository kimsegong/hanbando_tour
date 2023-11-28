package com.tour.hanbando.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.tour.hanbando.dao.MainMapper;
import com.tour.hanbando.dto.HotelDto;
import com.tour.hanbando.dto.PackageDto;
import com.tour.hanbando.util.MyPageUtils;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class MainServiceImpl implements MainService {
  private final MainMapper mainMapper;
  private final MyPageUtils myPageUtils;
  
  @Override
  public Map<String, Object> SearchPackageList(HttpServletRequest request) {
    
    String query = request.getParameter("query");
    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    int page = Integer.parseInt(opt.orElse("1"));
    Map<String, Object> onlyQuery = Map.of("query",query);
    int display = 9;
        
      int total = mainMapper.countSearchPackage(onlyQuery);
      
      myPageUtils.setPaging(page, total, display);
      
      Map<String, Object> map = Map.of("begin", myPageUtils.getBegin()
                                     , "end", myPageUtils.getEnd()
                                     ,"query",query);
  
     List<PackageDto> packageDto = mainMapper.searchPackageList(map);
     return Map.of("searchPackageList", packageDto
                 , "totalPage", myPageUtils.getTotalPage());
   
  }
  
  @Override
  public Map<String, Object> SearchHotelList(HttpServletRequest request) {
    String query = request.getParameter("query");
    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    int page = Integer.parseInt(opt.orElse("1"));
    Map<String, Object> onlyQuery = Map.of("query",query);
    int display = 9;
        
      int total = mainMapper.countSearchHotel(onlyQuery);
      
      myPageUtils.setPaging(page, total, display);
      
      Map<String, Object> map = Map.of("begin", myPageUtils.getBegin()
                                     , "end", myPageUtils.getEnd()
                                     ,"query",query);
  
     List<HotelDto> hotelDto = mainMapper.searchHotelList(map);
     return Map.of("searchHotelList", hotelDto
                 , "totalPage", myPageUtils.getTotalPage());
    
  }
  
  @Override
  public Map<String, Object> getBestPackage() {
    
    Map<String, Object> map = Map.of("bestPackageList",mainMapper.getBestPackage());
    
    return map;
  }
  
  @Override
  public Map<String, Object> getThemePackage() {
    int themeTotalNo = mainMapper.countTheme();
    
    int themeNo = (int)(Math.random()*themeTotalNo + 1);
    
    List<PackageDto> packageDto = mainMapper.getThemePackage(themeNo);
    
    return Map.of("themePackageList",packageDto);
  }
  
}



