package com.tour.hanbando.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.tour.hanbando.dao.HotelMapper;
import com.tour.hanbando.dao.MainMapper;
import com.tour.hanbando.dto.BannerImageDto;
import com.tour.hanbando.dto.HotelDto;
import com.tour.hanbando.dto.PackageDto;
import com.tour.hanbando.dto.RoompriceDto;
import com.tour.hanbando.util.MainFileUtil;
import com.tour.hanbando.util.MyPageUtils;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class MainServiceImpl implements MainService {
  private final MainMapper mainMapper;
  private final HotelMapper hotelMapper;
  private final MyPageUtils myPageUtils;
  private final MainFileUtil mainFileUtil; 
  
  @Transactional(readOnly = true)
  @Override
  public Map<String, Object> SearchPackageList(HttpServletRequest request) {
    
    String query = request.getParameter("query");
    String condition = request.getParameter("condition");
    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    int page = Integer.parseInt(opt.orElse("1"));
    Map<String, Object> onlyQuery = Map.of("query",query);
    int display = 9;
        
      int total = mainMapper.countSearchPackage(onlyQuery);
      
      myPageUtils.setPaging(page, total, display);
      
      Map<String, Object> map = Map.of("begin", myPageUtils.getBegin()
                                     , "end", myPageUtils.getEnd()
                                     , "condition", condition
                                     ,"query",query);
  
     List<PackageDto> packageDto = mainMapper.searchPackageList(map);
     return Map.of("searchPackageList", packageDto
                 , "totalPage", myPageUtils.getTotalPage()
                 , "count", total );
                     
   
  }
  @Transactional(readOnly = true)
  @Override
  public Map<String, Object> SearchHotelList(HttpServletRequest request) {
    String query = request.getParameter("query");
    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    int condition = Integer.parseInt(request.getParameter("condition"));
    int page = Integer.parseInt(opt.orElse("1"));
    Map<String, Object> onlyQuery = Map.of("query",query);
    int display = 9;
        
      int total = mainMapper.countSearchHotel(onlyQuery);
      
      myPageUtils.setPaging(page, total, display);
      Map<String, Object> map = Map.of("begin", myPageUtils.getBegin()
                                     , "end", myPageUtils.getEnd()
                                     , "condition", condition
                                     , "query",query);
      
      List<HotelDto> hotelDto = new ArrayList<>();
          
     if(condition == 0) {
        hotelDto = mainMapper.searchHotelList(map);
     } else {
       hotelDto = mainMapper.getSearchPriceHotelList(map);
     }
     
     return Map.of("searchHotelList", hotelDto
                 , "totalPage", myPageUtils.getTotalPage()
                 , "count" , total
                 ,"price", getPrice(hotelDto) );
    }
  
public List<Integer> getPrice(List<HotelDto> hotelDto){
    
    List<Integer> hPrice = new ArrayList<>();
    if(hotelDto.size() != 0) {
      
      List<RoompriceDto> roompriceDto = hotelMapper.getListPrice(hotelDto);
      /* 요금 구하기 */
      Date date = new Date();
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
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
   
    
    return hPrice;
  }
  
  
  @Transactional(readOnly = true)
  @Override
  public Map<String, Object> getBestPackage() {
    
    Map<String, Object> map = Map.of("bestPackageList",mainMapper.getBestPackage());
    
    return map;
  }
  
  @Transactional(readOnly = true)
  @Override
  public Map<String, Object> getThemePackage() {
    int themeTotalNo = mainMapper.countTheme();
    
    int themeNo = (int)(Math.random()*themeTotalNo + 1);
    
    List<PackageDto> packageDto = mainMapper.getThemePackage(themeNo);
    
    return Map.of("themePackageList",packageDto);
  }
  
  @Transactional(readOnly = true)
  @Override
  public void bannerList(Model model) {
    
    List<BannerImageDto> bannerImage = mainMapper.getBannerImage();
    model.addAttribute("bannerList", bannerImage);
  }
  
  
  
  @Override
  public int addBannerImage(MultipartHttpServletRequest multipartRequest) throws Exception {
    
    MultipartFile files = multipartRequest.getFile("files");
    int bannerNo=Integer.parseInt(multipartRequest.getParameter("bannerNo"));
    int state = Integer.parseInt(multipartRequest.getParameter("state"));
    // 첨부 없을 때 : [MultipartFile[field="files", filename=, contentType=application/octet-stream, size=0]]
    // 첨부 1개     : [MultipartFile[field="files", filename="animal1.jpg", contentType=image/jpeg, size=123456]]
    
    int attachCount;
    if(files.getSize() == 0) {
      attachCount = 1;
    } else {
      attachCount = 0;
    }

      int oldState = mainMapper.getNoBannerImage(bannerNo).getState();
   
      if((files != null && !files.isEmpty()) || state != oldState) {
        
        String path = mainFileUtil.getUploadPath();
        File dir = new File(path);
        if(!dir.exists()) {
          dir.mkdirs();
        }
        
        String bannerPath = mainFileUtil.getUploadPath();
        String originalName = files.getOriginalFilename();
        String filesystemName = mainFileUtil.getFilesystemName(originalName);
        String existOriginalName = mainMapper.getNoBannerImage(bannerNo).getOriginalName();
        String existFileSystemName = mainMapper.getNoBannerImage(bannerNo).getFilesystemName();
        if(!existOriginalName.equals(originalName)) {
          
          filesystemName = mainFileUtil.getFilesystemName(files.getOriginalFilename());
          File file = new File(dir, filesystemName);
          
          files.transferTo(file);
        } else {
          
          originalName = existOriginalName;
          filesystemName = existFileSystemName;
        }
        
        BannerImageDto bannerImageDto = BannerImageDto.builder()
                                          .bannerNo(bannerNo)
                                          .bannerPath(bannerPath)
                                          .originalName(originalName)
                                          .filesystemName(filesystemName)
                                          .state(state)
                                          .build();
        
        attachCount += mainMapper.updateBannerImage(bannerImageDto);
        }  // if
        
        return attachCount;
  }
  @Override
  public int modifyAddress(HttpServletRequest request) {
    int bannerNo = Integer.parseInt(request.getParameter("bannerNo"));
    String linkedAddress = request.getParameter("address");
    
    BannerImageDto bannerImageDto = BannerImageDto.builder()
                                       .bannerNo(bannerNo)
                                       .linkedAddress(linkedAddress)
                                       .build();
    int modifyResult = mainMapper.updateAddress(bannerImageDto);
    
    return modifyResult;
  }
  
  @Override
  public Map<String, Object> getMainBannerList() {
    Map<String, Object> map = Map.of("banner",mainMapper.getBannerImage());
    return map;
  }
  
}



