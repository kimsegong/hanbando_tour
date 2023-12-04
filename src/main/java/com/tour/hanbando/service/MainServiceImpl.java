package com.tour.hanbando.service;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.tour.hanbando.dao.MainMapper;
import com.tour.hanbando.dto.BannerImageDto;
import com.tour.hanbando.dto.HotelDto;
import com.tour.hanbando.dto.PackageDto;
import com.tour.hanbando.util.MainFileUtil;
import com.tour.hanbando.util.MyPageUtils;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class MainServiceImpl implements MainService {
  private final MainMapper mainMapper;
  private final MyPageUtils myPageUtils;
  private final MainFileUtil mainFileUtil; 
  
  @Transactional(readOnly = true)
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
  @Transactional(readOnly = true)
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
    // 첨부 없을 때 : [MultipartFile[field="files", filename=, contentType=application/octet-stream, size=0]]
    // 첨부 1개     : [MultipartFile[field="files", filename="animal1.jpg", contentType=image/jpeg, size=123456]]
    
    int attachCount;
    if(files.getSize() == 0) {
      attachCount = 1;
    } else {
      attachCount = 0;
    }

      if(files != null && !files.isEmpty()) {
        
        String path = mainFileUtil.getUploadPath();
        File dir = new File(path);
        if(!dir.exists()) {
          dir.mkdirs();
        }
        
        List<BannerImageDto> bannerImage = mainMapper.getBannerImage();
        String bannerPath = mainFileUtil.getUploadPath();
        String originalName = files.getOriginalFilename();
        String filesystemName = mainFileUtil.getFilesystemName(originalName);
        String existOriginalName = mainMapper.getNoBannerImage(bannerNo).getOriginalName();
        String existFileSystemName = mainMapper.getNoBannerImage(bannerNo).getFilesystemName();
        int state = Integer.parseInt(multipartRequest.getParameter("state"));
        
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



