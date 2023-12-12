package com.tour.hanbando.service;

import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.tour.hanbando.dao.HotelMapper;
import com.tour.hanbando.dao.PackageMapper;
import com.tour.hanbando.dto.FacilitiesDto;
import com.tour.hanbando.dto.HeartDto;
import com.tour.hanbando.dto.HotelDto;
import com.tour.hanbando.dto.HotelImageDto;
import com.tour.hanbando.dto.RegionDto;
import com.tour.hanbando.dto.ReserveDto;
import com.tour.hanbando.dto.ReviewDto;
import com.tour.hanbando.dto.RoomFeatureDto;
import com.tour.hanbando.dto.RoompriceDto;
import com.tour.hanbando.dto.RoomtypeDto;
import com.tour.hanbando.dto.UserDto;
import com.tour.hanbando.util.HotelFileUtils;
import com.tour.hanbando.util.MyPageUtils;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class HotelServiceImpl implements HotelService {
  private final HotelMapper hotelMapper;
  private final PackageMapper packageMapper;
  private final MyPageUtils myPageUtils;
  private final HotelFileUtils hotelFileUtils;
  
  @Override
  public Map<String, Object> getHotelList(HttpServletRequest request) {
    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    int page = Integer.parseInt(opt.orElse("1"));
    int total = hotelMapper.countHotel(0);
    int display = 9;
    
    myPageUtils.setPaging(page, total, display);
    
    int end = myPageUtils.getEnd();
    int begin = myPageUtils.getBegin();
    
    int btnVal = Integer.parseInt(request.getParameter("btnVal"));
    
    Map<String, Object> map = Map.of("begin", begin
                                   , "end", end
                                   , "btnVal", btnVal);
    
    List<HotelDto> hotelDto = new ArrayList<>();
    int recommend = 0;
    switch (btnVal) {
    case 0 : hotelDto = hotelMapper.selectHotelList(map);
      break;
    case 1 : hotelDto = hotelMapper.getRecommendHotelList(map); recommend = 1;
      break;
    case 2 : hotelDto = hotelMapper.getReviewHotelList(map);
      break;
    case 3 : hotelDto = hotelMapper.getPriceHotelList(map);
      break;
    case 4 : hotelDto = hotelMapper.getPriceHotelList(map);
      break;
    }
    
    int count = hotelMapper.countHotel(recommend);
    
    List<Integer> hPrice = getPrice(hotelDto);
    
    Map<String, Object> hotel = Map.of("hotelList", hotelDto
                                      ,"price", hPrice
                                      ,"count", count
                                      ,"totalPage", myPageUtils.getTotalPage()
                                      );
    
    return hotel;
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
  
  @Override
  public int increseHit(int hotelNo) {
    return hotelMapper.hotelHit(hotelNo);
  }
  
  @Override
  public void regionList(Model model) {
    model.addAttribute("region", hotelMapper.getRegion());
  }
  
  
  /************************이거 인서트 할때 룸 추가하면 보여줄때 쓰는 서비스********************************/
  
  @Override
  public Map<String, Object> hotelRoomList(HttpServletRequest request) {
    
    int hotelNo = Integer.parseInt(request.getParameter("hotelNo"));
    
    List<RoomtypeDto> roomtypeList = hotelMapper.getRoomtype(hotelNo);
    List<RoomFeatureDto> roomFeatureList = hotelMapper.getRoomFeature(roomtypeList);
    List<HotelImageDto> hotelImageList = hotelMapper.getHotelImage(hotelNo);
    List<RoompriceDto> roompriceList = hotelMapper.getPrice(RoomtypeDto.builder().hotelNo(hotelNo).build());
    
    return Map.of( "roomFeatureList", roomFeatureList,
                   "hotelImageList", hotelImageList,
                   "roompriceList", roompriceList,
                   "roomtypeList", roomtypeList);
   
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
    String bsDate = multipartRequest.getParameter("bsDate").replace("-", "/"); 
    String beDate = multipartRequest.getParameter("beDate").replace("-", "/"); 
    int junPrice = Integer.parseInt(multipartRequest.getParameter("junPrice"));
    String jsDate = multipartRequest.getParameter("jsDate").replace("-", "/"); 
    String jeDate = multipartRequest.getParameter("jeDate").replace("-", "/");
    int sungPrice = Integer.parseInt(multipartRequest.getParameter("sungPrice"));
    String ssDate = multipartRequest.getParameter("ssDate").replace("-", "/"); 
    String seDate = multipartRequest.getParameter("seDate").replace("-", "/");
    
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
    /*  가져와야하는 거  room price roomimage heart */  
    
    HotelDto hotelDto = hotelMapper.getHotel(hotelNo);
    FacilitiesDto facilitiesDto = hotelMapper.getFacilityies(hotelNo);
    List<HotelImageDto> hotelImageDto = hotelMapper.getHotelImage(hotelNo);
    
    List<RoomtypeDto> roomtypeDto = hotelMapper.getRoomtype(hotelNo);
    List<RoompriceDto> roompriceDto = hotelMapper.getPrice(RoomtypeDto.builder().hotelNo(hotelNo).build());
    
    List<HotelDto> hotel = new ArrayList<>();
    hotel.add(hotelDto); // 가격 가져올려고 
    List<RoomFeatureDto> roomFeatureDto = hotelMapper.getRoomFeature(roomtypeDto);
    List<Integer> price = getPrice(hotel);
    List<Integer> countReserveRoom = new ArrayList<>();
    for(int i = 0; i < roomtypeDto.size(); i++) {
      countReserveRoom.add(i, getDate(roomtypeDto.get(i)));
    }
    
    model.addAttribute("hotel", hotelDto);
    model.addAttribute("hotelImage", hotelImageDto);
    model.addAttribute("fac", facilitiesDto);
    model.addAttribute("lowPrice", price);
    model.addAttribute("price", roompriceDto);
    model.addAttribute("countReserveRoom", countReserveRoom);
    model.addAttribute("roomType", roomtypeDto);
    model.addAttribute("roomFeature", roomFeatureDto);
  }
  
  public int getDate(RoomtypeDto roomtypeDto){
    
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    Calendar calendar = Calendar.getInstance();
    String today = sdf.format(calendar.getTime());
    calendar.add(Calendar.DATE, +1);
    String nextDay = sdf.format(calendar.getTime()); 
    Map<String, Object> map = Map.of("roomNo", roomtypeDto.getRoomNo() ,
                                      "checkin", today ,  
                                      "checkout", nextDay);
    
    int sample = hotelMapper.countReserveRoom(map);
    
    return sample;
  }

  @Override
  public Map<String, Object> addReview(HttpServletRequest request) {
  
    String reviewContents = request.getParameter("reviewContents");
    int userNo = Integer.parseInt(request.getParameter("userNo"));
    int hotelNo = Integer.parseInt(request.getParameter("hotelNo"));
    int star = Integer.parseInt(request.getParameter("star"));
    
    ReviewDto review = ReviewDto.builder()                            
                          .reviewContents(reviewContents)
                          .hotelNo(hotelNo)
                          .star(star)
                          .userDto(UserDto.builder()
                                    .userNo(userNo)
                                    .build())                            
                          .build();

    int addReviewResult = hotelMapper.insertReview(review);
    
    return Map.of("addReviewResult", addReviewResult);
    
  }
  
  @Transactional(readOnly=true)
  @Override
  public List<ReserveDto> getReserveUser(int hotelNo) {
      List<ReserveDto> reserve = hotelMapper.getReserve(hotelNo);
      return reserve ;
  }
  
  @Transactional(readOnly=true)
  @Override
  public Map<String, Object> loadReviewList(HttpServletRequest request) {
  
    
    int hotelNo = Integer.parseInt(request.getParameter("hotelNo"));
    int sort = Integer.parseInt(request.getParameter("sort"));
    
    String pageParameter = request.getParameter("page");
    int page = 1;  // 기본값 설정
    if (pageParameter != null && !pageParameter.isEmpty()) {
        try {
            page = Integer.parseInt(pageParameter);
        } catch (NumberFormatException e) {             
            e.printStackTrace();  
        }
    }
    int total = hotelMapper.getReviewCount(hotelNo);
    int display = 10;
    
    myPageUtils.setPaging(page, total, display);
    
    Map<String, Object> map = Map.of("hotelNo", hotelNo
                                   , "sort", sort
                                   , "begin", myPageUtils.getBegin()
                                   , "end", myPageUtils.getEnd());
    
    List<ReviewDto> reviewList = hotelMapper.getReviewList(map);
    String paging = myPageUtils.getAjaxPaging();
    Map<String, Object> result = new HashMap<String, Object>();
    result.put("reviewList", reviewList);
    result.put("paging", paging);
    return result;
  }
  
  @Override
  public double getAverageRating(int hotelNo) {
    Optional<Double> opt = Optional.ofNullable(hotelMapper.starAve(hotelNo));
    Double star = opt.orElse(0.0);
    return star;
  }
  
  @Override
  public Map<String, Object> removeReview(int reviewNo) {
      int removeResult = hotelMapper.deleteReview(reviewNo);
      return Map.of("removeResult", removeResult);
   }
  
  @Override
  public int getHeart(HttpServletRequest request) {
    
    int userNo =Integer.parseInt(request.getParameter("userNo"));
    int hotelNo = Integer.parseInt(request.getParameter("hotelNo"));
    
    HeartDto heartDto = HeartDto.builder()
                            .userDto(UserDto.builder().userNo(userNo).build())
                            .hotelDto(HotelDto.builder().hotelNo(hotelNo).build())
                            .build();
    
    int heartStatus = hotelMapper.getCountHeart(heartDto);
    
    if(heartStatus == 1) {
      hotelMapper.deleteHeart(heartDto);
    } else if(heartStatus == 0) {
      hotelMapper.insertHeart(heartDto);
    }
    return hotelMapper.getCountHeart(heartDto);
  }
  
  @Override
  public void getHeartHotel(HttpServletRequest request, Model model) {
    
    
    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    int page = Integer.parseInt(opt.orElse("1"));
    int display = 10;
    int userNo = Integer.parseInt(request.getParameter("userNo"));
    int total = packageMapper.getHeartCount(userNo);
    
    myPageUtils.setPaging(page, total, display);
    
    Map<String, Object> map = Map.of("begin", myPageUtils.getBegin()
                                   , "end", myPageUtils.getEnd()
                                   , "userNo", userNo);
    
    List<HeartDto> heartHotelList = hotelMapper.selectHotelHeartList(map);
    
    List<HotelDto> hotelList = new ArrayList<>();
    for(HeartDto heart : heartHotelList) {
      hotelList.add(heart.getHotelDto());
    }
    List<Integer> price = getPrice(hotelList);
    
    model.addAttribute("heartHotelList", heartHotelList);
    model.addAttribute("price", price);
    String params = "userNo=" + request.getParameter("userNo");
    model.addAttribute("paging", myPageUtils.getMvcPaging(request.getContextPath() + "/user/heart.do", params));
    model.addAttribute("beginNo", total - (page - 1) * display); 

  }
  
  
  @Override
  public Map<String, Object> removeHotelHeart(int hotelNo) {
      int removeHotelHeartResult = hotelMapper.deleteHotelHeart(hotelNo);
      return Map.of("removeHotelHeartResult", removeHotelHeartResult);
    }

  @Override
  public int removehotel(int hotelNo) {
    int deleteResult = hotelMapper.deleteHotel(hotelNo);
    return deleteResult;
  }
  
  @Override
  public Map<String, Object> getFinalPrice(HttpServletRequest request) {
   int roomNo = Integer.parseInt(request.getParameter("roomNo"));
   RoomtypeDto roomtypeDto = hotelMapper.roomtype(roomNo);
   
   String date = request.getParameter("date");
   DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyy/MM/dd");
   
   LocalDate checkin = getcheckin(date);
   LocalDate checkout = getcheckout(date);
   
   int totalPrice = finalPrice(makeDateList(checkin, checkout), roomNo);
   String roomName = roomtypeDto.getRoomName();
   Map<String, Object> reserve = Map.of("totalPrice", totalPrice, "roomName", roomName, 
                                         "checkin",checkin.format(fmt),"checkout",checkout.format(fmt));
   return reserve;
  }
  
  private LocalDate getcheckin(String date) {
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    return LocalDate.parse(date.substring(0, 10), fmt);
  }
  private LocalDate getcheckout(String date) {
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    return LocalDate.parse(date.substring(13), fmt);
  }
  
  private List<LocalDate> makeDateList(LocalDate checkIn, LocalDate checkOut){
    List<LocalDate> allDate =  checkIn.datesUntil(checkOut).collect(Collectors.toList());
    return allDate;
  }
  
  private int finalPrice(List<LocalDate> allDate, int roomNo) {
    List<RoompriceDto> roompriceDto = hotelMapper.getPrice(RoomtypeDto.builder().roomNo(roomNo).build());
    RoompriceDto price = roompriceDto.get(0);
    
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd");    
    int totalPrice = 0;
    for(LocalDate eachDate: allDate) {
        int date = Integer.parseInt(eachDate.format(fmt));
      
        int biStart = Integer.parseInt(price.getBsDate().replace("/", ""));
        int biEnd = Integer.parseInt(price.getBeDate().replace("/", ""));
        
        int jsStart = Integer.parseInt(price.getJsDate().replace("/", ""));
        int jsEnd = Integer.parseInt(price.getJeDate().replace("/", ""));
        
        int ssStart = Integer.parseInt(price.getSsDate().replace("/", ""));
        int ssEnd = Integer.parseInt(price.getSeDate().replace("/", ""));
        
        if(biStart <= date && date <= biEnd) {
          totalPrice += price.getBiPrice();
        }else if(jsStart <= date && date <= jsEnd) {
          totalPrice +=  price.getJunPrice();
        }else if(ssStart <= date && date <= ssEnd) {
          totalPrice +=  price.getSungPrice();
        } else {
          totalPrice =  0;
        }
      }
    
    return totalPrice;
  }
  
  
  @Override
  public int modifyHotel(int hotelNo) {
    return 0;
  }
  
  
  
}
