package com.tour.hanbando.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.tour.hanbando.dao.MainMapper;
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
  public Map<String, Object> headerSearch(HttpServletRequest request) {
    
    String query = request.getParameter("search");
    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    int page = Integer.parseInt(opt.orElse("1"));
    Map<String, Object> onlyQuery = Map.of("query",query);
    int total = mainMapper.countSearchPackage(onlyQuery);
    int display = 9;
    
    myPageUtils.setPaging(page, total, display);
    
    Map<String, Object> map = Map.of("begin", myPageUtils.getBegin()
                                   , "end", myPageUtils.getEnd()
                                   ,"query",query);
    
   List<PackageDto> packageDto = mainMapper.searchPackageList(map);
   return Map.of("sPackageList", packageDto
               , "totalPage", myPageUtils.getTotalPage());

  }
  
  
}
