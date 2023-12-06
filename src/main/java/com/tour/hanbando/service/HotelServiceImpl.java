package com.tour.hanbando.service;

import java.io.File;
import java.nio.file.Files;
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
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tour.hanbando.dao.HotelMapper;
import com.tour.hanbando.dto.FacilitiesDto;
import com.tour.hanbando.dto.HotelDto;
import com.tour.hanbando.dto.HotelImageDto;
import com.tour.hanbando.dto.RegionDto;
import com.tour.hanbando.dto.RoomFeatureDto;
import com.tour.hanbando.dto.RoompriceDto;
import com.tour.hanbando.dto.RoomtypeDto;
import com.tour.hanbando.util.HotelFileUtils;
import com.tour.hanbando.util.MyPageUtils;

import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;

@Transactional
@RequiredArgsConstructor
@Service
public class HotelServiceImpl implements HotelService {
  private final HotelMapper hotelMapper;
  private final MyPageUtils myPageUtils;
  private final HotelFileUtils hotelFileUtils;
  
  @Override
  public Map<String, Object> getHotelList(HttpServletRequest request) {
    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    int page = Integer.parseInt(opt.orElse("1"));
    int total = hotelMapper.countHotel();
    int display = 9;
    
    myPageUtils.setPaging(page, total, display);
    
    int end = myPageUtils.getEnd();
    int begin = myPageUtils.getBegin();
    
    int btnVal = Integer.parseInt(request.getParameter("btnVal"));
    
    Map<String, Object> map = Map.of("begin", begin
                                   , "end", end
                                   , "btnVal", btnVal);
              
    
    List<Integer> hPrice = new ArrayList<>();
    List<HotelDto> hotelDto = new ArrayList<>();
    
    
    switch (btnVal) {
    case 0 : hotelDto = hotelMapper.selectHotelList(map);
      break;
    case 1 : hotelDto = hotelMapper.getRecommendHotelList(map);
      break;
    case 2 : hotelDto = hotelMapper.getReviewHotelList(map);
      break;
    case 3 : hotelDto = hotelMapper.getPriceHotelList(map);
      break;
    case 4 :hotelDto = hotelMapper.getPriceHotelList(map);
      break;
    }
    
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
  public int increseHit(int hotelNo) {
    return hotelMapper.hotelHit(hotelNo);
  }
  
  @Override
  public void regionList(Model model) {
    model.addAttribute("region", hotelMapper.getRegion());
  }
  
  @Override
  public void hotelRoomList(HttpServletRequest request, Model model) {
    
    int hotelNo = Integer.parseInt(request.getParameter("hotelNo"));
    
    List<RoomtypeDto> roomtypeDto = hotelMapper.getRoomtype(hotelNo);
    hotelMapper.getRoomFeature(roomtypeDto);
    hotelMapper.getRoomImage(roomtypeDto);
    hotelMapper.getPrice(roomtypeDto);
    
  }
  
  @Override
  public void makeHotelNo(Model model) {
      int hotelNo =  hotelMapper.getHotelNo();
      hotelMapper.insertHotelNo(hotelNo);
      model.addAttribute("hotelNo", hotelNo);
   }
  
  @Override
  public boolean writeRoom(MultipartHttpServletRequest multipartRequest, List<MultipartFile> files) throws Exception {
    
    int hotelNo = Integer.parseInt(multipartRequest.getParameter("hotelNo"));
    String roomName = multipartRequest.getParameter("roomName");
    String roomDetail = multipartRequest.getParameter("roomDetail");
    String rView = multipartRequest.getParameter("rView");
    String bed = multipartRequest.getParameter("bed");
    String shower = multipartRequest.getParameter("shower");
    int rSize = Integer.parseInt(multipartRequest.getParameter("rSize"));
    Optional<String> optSmoke = Optional.ofNullable(multipartRequest.getParameter("smoke"));
    int smoke = Integer.parseInt(optSmoke.orElse("0"));
    Optional<String> optBleakfast = Optional.ofNullable(multipartRequest.getParameter("bleakfast"));
    int bleakfast = Integer.parseInt(optBleakfast.orElse("0"));
    int people = Integer.parseInt(multipartRequest.getParameter("people"));
    int roomMany = Integer.parseInt(multipartRequest.getParameter("roomMany"));
    
    Optional<String> optTowel = Optional.ofNullable(multipartRequest.getParameter("towel"));
    Optional<String> optWater = Optional.ofNullable(multipartRequest.getParameter("water"));
    Optional<String> optCoffee = Optional.ofNullable(multipartRequest.getParameter("coffee"));
    Optional<String> optDrier = Optional.ofNullable(multipartRequest.getParameter("drier"));
    Optional<String> optIron = Optional.ofNullable(multipartRequest.getParameter("iron"));
    Optional<String> optMinibar = Optional.ofNullable(multipartRequest.getParameter("minibar"));
    int towel = Integer.parseInt(optTowel.orElse("0"));
    int water = Integer.parseInt(optWater.orElse("0"));
    int coffee = Integer.parseInt(optCoffee.orElse("0"));
    int drier = Integer.parseInt(optDrier.orElse("0"));
    int iron = Integer.parseInt(optIron.orElse("0"));
    int minibar = Integer.parseInt(optMinibar.orElse("0"));
    
    System.out.println(minibar);
    int biPrice = Integer.parseInt(multipartRequest.getParameter("biPrice"));
    String bsDate = multipartRequest.getParameter("bsDate"); 
    String beDate = multipartRequest.getParameter("beDate"); 
    int junPrice = Integer.parseInt(multipartRequest.getParameter("junPrice"));
    String jsDate = multipartRequest.getParameter("jsDate"); 
    String jeDate = multipartRequest.getParameter("jeDate");
    int sungPrice = Integer.parseInt(multipartRequest.getParameter("sungPrice"));
    String ssDate = multipartRequest.getParameter("ssDate"); 
    String seDate = multipartRequest.getParameter("seDate");
    
    RoomtypeDto roomtypeDto = RoomtypeDto.builder()
                                .hotelNo(hotelNo)
                                .roomName(roomName)
                                .roomDetail(roomDetail)
                                .rView(rView)
                                .shower(shower)
                                .bed(bed)
                                .smoke(smoke)
                                .bleakfast(bleakfast)
                                .people(people)
                                .roomMany(roomMany)
                                .rSize(rSize)
                                .build();
    
   int insertResult = hotelMapper.insertRoomtype(roomtypeDto); 
   
   RoomFeatureDto roomFeatureDto = RoomFeatureDto.builder()
                                     .towel(towel)
                                     .coffee(coffee)
                                     .drier(drier)
                                     .iron(iron)
                                     .minibar(minibar)
                                     .water(water)
                                     .build();
   
   insertResult += hotelMapper.insertRoomFeature(roomFeatureDto);
   
   RoompriceDto roompriceDto = RoompriceDto.builder()
                                     .hotelNo(hotelNo)
                                     .biPrice(biPrice)
                                     .bsDate(bsDate)
                                     .beDate(beDate)
                                     .junPrice(junPrice)
                                     .jsDate(jsDate)
                                     .jeDate(jeDate)
                                     .sungPrice(sungPrice)
                                     .ssDate(ssDate)
                                     .seDate(seDate)
                                     .build();
   
   insertResult += hotelMapper.insertRoomPrice(roompriceDto);                                  
   
    
    //List<MultipartFile> files = multipartRequest.getFiles("files");
    
    // 첨부 없을 때 : [MultipartFile[field="files", filename=, contentType=application/octet-stream, size=0]]
    // 첨부 1개     : [MultipartFile[field="files", filename="animal1.jpg", contentType=image/jpeg, size=123456]]
    
    int attachCount;
    if(files.get(0).getSize() == 0) {
      attachCount = 1;
    } else {
      attachCount = 0;
    }
    
    
    for(MultipartFile multipartFile : files) {
      
      if(multipartFile != null && !multipartFile.isEmpty()) {
        
        String path = hotelFileUtils.getUploadPath();
        File dir = new File(path);
        if(!dir.exists()) {
          dir.mkdirs();
        }
        
        String originalFilename = multipartFile.getOriginalFilename();
        String filesystemName = hotelFileUtils.getFilesystemName(originalFilename);
        File file = new File(dir, filesystemName);
        
        multipartFile.transferTo(file);
        
        String contentType = Files.probeContentType(file.toPath());  // 이미지의 Content-Type은 image/jpeg, image/png 등 image로 시작한다.
        
        HotelImageDto hotelImageDto = HotelImageDto.builder()
                                         .hotelNo(hotelNo)
                                         .thumbnail(0)
                                         .roomNo(0)
                                         .originalName(originalFilename)
                                         .filesystemName(filesystemName)
                                         .imagePath(path)
                                         .build();
        
        attachCount += hotelMapper.insertRoomImage(hotelImageDto);
        
      }  // if
      
    }  // for
    
    return (insertResult == 3) && (files.size() == attachCount);
  }
  
  @Override
  public boolean writeHotel(MultipartHttpServletRequest multipartRequest) throws Exception {
    
    int hotelNo = Integer.parseInt(multipartRequest.getParameter("hotelNo"));
    
    String hotelName = multipartRequest.getParameter("hotelName");
    int recommendStatus = Integer.parseInt(multipartRequest.getParameter("recommendStatus"));
    int status = Integer.parseInt(multipartRequest.getParameter("status"));
    int regionNo = Integer.parseInt(multipartRequest.getParameter("regionNo"));
    String hEmail = multipartRequest.getParameter("hEmail");
    String phoneNumber = multipartRequest.getParameter("phoneNumber");
    String hotelAddress = multipartRequest.getParameter("hotelAddress");
    Double latitude = Double.parseDouble(multipartRequest.getParameter("latitude"));
    Double longitude = Double.parseDouble(multipartRequest.getParameter("longitude"));
    String hotelDetail = multipartRequest.getParameter("hotelDetail");
    
    Optional<String> optMorning = Optional.ofNullable(multipartRequest.getParameter("morning"));
    Optional<String> optPool = Optional.ofNullable(multipartRequest.getParameter("pool"));
    Optional<String> optSauna = Optional.ofNullable(multipartRequest.getParameter("sauna"));
    Optional<String> optLounge = Optional.ofNullable(multipartRequest.getParameter("lounge"));
    Optional<String> optRoomService = Optional.ofNullable(multipartRequest.getParameter("roomservice"));
    
    int morning = Integer.parseInt(optMorning.orElse("0"));
    int pool = Integer.parseInt(optPool.orElse("0"));
    int sauna = Integer.parseInt(optSauna.orElse("0"));
    int lounge = Integer.parseInt(optLounge.orElse("0"));
    int roomservice = Integer.parseInt(optRoomService.orElse("0"));
    
    HotelDto hotelDto = HotelDto.builder()
                              .hotelNo(hotelNo)
                              .hotelName(hotelName)
                              .hotelDetail(hotelDetail)
                              .phoneNumber(phoneNumber)
                              .hEmail(hEmail)
                              .hotelAddress(hotelAddress)
                              .latitude(latitude)
                              .longitude(longitude)
                              .recommendStatus(recommendStatus)
                              .status(status)
                              .regionDto(RegionDto.builder().regionNo(regionNo).build())
                              .build();
    
    FacilitiesDto facilitiesDto = FacilitiesDto.builder()
                                        .hotelNo(hotelNo)
                                        .lounge(lounge)
                                        .morning(morning)
                                        .pool(pool)
                                        .roomservice(roomservice)
                                        .sauna(sauna)
                                        .build();
                                        
    int insertResult = hotelMapper.updateHotel(hotelDto);
    
    insertResult += hotelMapper.insertFacilities(facilitiesDto);
                                        
    List<MultipartFile> files = multipartRequest.getFiles("files");
    
    // 첨부 없을 때 : [MultipartFile[field="files", filename=, contentType=application/octet-stream, size=0]]
    // 첨부 1개     : [MultipartFile[field="files", filename="animal1.jpg", contentType=image/jpeg, size=123456]]
    
    int attachCount;
    if(files.get(0).getSize() == 0) {
      attachCount = 1;
    } else {
      attachCount = 0;
    }
    
    for(MultipartFile multipartFile : files) {
      
      if(multipartFile != null && !multipartFile.isEmpty()) {
        
        String path = hotelFileUtils.getUploadPath();
        File dir = new File(path);
        if(!dir.exists()) {
          dir.mkdirs();
        }
        
        String originalFilename = multipartFile.getOriginalFilename();
        String filesystemName = hotelFileUtils.getFilesystemName(originalFilename);
        File file = new File(dir, filesystemName);
        
        multipartFile.transferTo(file);
        
        String contentType = Files.probeContentType(file.toPath());  // 이미지의 Content-Type은 image/jpeg, image/png 등 image로 시작한다.
        
        int thumbnail = 0;
        if(originalFilename.contains("mainList")) {
          thumbnail = 1;
        }
        
        HotelImageDto hotelImageDto = HotelImageDto.builder()
                                         .hotelNo(hotelNo)
                                         .thumbnail(thumbnail)
                                         .originalName(originalFilename)
                                         .filesystemName(filesystemName)
                                         .imagePath(path)
                                         .build();
        
        attachCount += hotelMapper.insertRoomImage(hotelImageDto);
        
      }  // if
      
    }  // for
    
    return (insertResult == 3) && (files.size() == attachCount);
  }
  
  @Override
  public void hoteDetail(HttpServletRequest request, int hotelNo, Model model) {
    /*  가져와야하는 거 hotel facility image roomfeature, room price roomimage heart */  
    
    HotelDto hotelDto = hotelMapper.getHotel(hotelNo);
    List<HotelImageDto> hotelImageDto = hotelMapper.getHotelImage(hotelNo);
    model.addAttribute("hotel", hotelDto);
    model.addAttribute("hotelImage", hotelImageDto);
    
    
    
  }
  
  
  
}
