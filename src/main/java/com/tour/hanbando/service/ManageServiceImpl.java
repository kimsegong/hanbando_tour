package com.tour.hanbando.service;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.tour.hanbando.dao.ManageMapper;
import com.tour.hanbando.dao.UserMapper;
import com.tour.hanbando.dto.HotelDto;
import com.tour.hanbando.dto.InactiveUserDto;
import com.tour.hanbando.dto.LeaveUserDto;
import com.tour.hanbando.dto.PackageDto;
import com.tour.hanbando.dto.RegionDto;
import com.tour.hanbando.dto.ReserveDto;
import com.tour.hanbando.dto.ReviewDto;
import com.tour.hanbando.dto.RoompriceDto;
import com.tour.hanbando.dto.RoomtypeDto;
import com.tour.hanbando.dto.UserDto;
import com.tour.hanbando.util.MyPageUtils;
import com.tour.hanbando.util.MySecurityUtils;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class ManageServiceImpl implements ManageService {

  private final UserMapper userMapper;
  private final ManageMapper manageMapper;
  private final MyPageUtils myPageUtils;
  private final MySecurityUtils mySecurityUtils;
  
  
  /**
   * 기존 회원 목록
   * MVC 페이징 처리
   * 
   * @author 심희수
   * @param request
   * @param model
   */
  @Transactional(readOnly=true)
  @Override
  public void loadUserList(HttpServletRequest request, Model model) {

    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    int page = Integer.parseInt(opt.orElse("1"));
    int total = manageMapper.getUserCount();
    int display = 20;
    
    myPageUtils.setPaging(page, total, display);
    
    Map<String, Object> map = Map.of("begin", myPageUtils.getBegin()
                                   , "end", myPageUtils.getEnd());
    
    List<UserDto> userList = manageMapper.getUserList(map);
    
    model.addAttribute("userList", userList);
    model.addAttribute("paging", myPageUtils.getMvcPaging(request.getContextPath() + "/manage/userList.do"));
    model.addAttribute("beginNo", total - (page - 1) * display);
    model.addAttribute("total", total);
    
  }
  
  /**
   * 기존 회원 검색
   * 
   * @author 심희수
   * @param request
   * @param model
   */
  @Transactional(readOnly=true)
  @Override
  public void loadSearchUserList(HttpServletRequest request, Model model) {
    
    String column = request.getParameter("column");
    String query = request.getParameter("query");
    
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("column", column);
    map.put("query", query);
    
    int total = manageMapper.getSearchUserCount(map);
    
    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    String strPage = opt.orElse("1");
    int page = Integer.parseInt(strPage);
    int display = 20;
    
    myPageUtils.setPaging(page, total, display);
    
    map.put("begin", myPageUtils.getBegin());
    map.put("end", myPageUtils.getEnd());
    
    List<UserDto> userList = manageMapper.getSearchUser(map);
    
    model.addAttribute("userList", userList);
    model.addAttribute("paging", myPageUtils.getMvcPaging(request.getContextPath() + "/manage/userSearchList.do", "column=" + column + "&query=" + query));
    model.addAttribute("beginNo", total - (page - 1) * display);
    model.addAttribute("total", total);
    
  }
  
  /**
   * 기존 회원 상세
   * 
   * @author 심희수
   * @param userNo 회원번호
   * @return 회원번호를 Map에 담아서 반환
   */
  @Transactional(readOnly=true)
  @Override
  public UserDto getUser(int userNo) {
    return userMapper.getUser(Map.of("userNo", userNo));
  }
  
  /**
   * 기존 회원 정보 수정
   * 
   * @author 심희수
   * @param request
   * @return 수정된 데이터 수 반환
   */
  @Override
  public ResponseEntity<Map<String, Object>> modifyUser(HttpServletRequest request) {
    
    String name = mySecurityUtils.preventXSS(request.getParameter("name"));
    String gender = request.getParameter("gender");
    String mobile = request.getParameter("mobile");
    String postcode = request.getParameter("postcode");
    String roadAddress = request.getParameter("roadAddress");
    String jibunAddress = request.getParameter("jibunAddress");
    String detailAddress = mySecurityUtils.preventXSS(request.getParameter("detailAddress"));
    String event = request.getParameter("event");
    int agree = event.equals("on") ? 1 : 0;
    int userNo = Integer.parseInt(request.getParameter("userNo"));
    
    UserDto user = UserDto.builder()
                    .name(name)
                    .gender(gender)
                    .mobile(mobile)
                    .postcode(postcode)
                    .roadAddress(roadAddress)
                    .jibunAddress(jibunAddress)
                    .detailAddress(detailAddress)
                    .agree(agree)
                    .userNo(userNo)
                    .build();
    int modifyResult = userMapper.updateUser(user);
    
    return new ResponseEntity<Map<String, Object>>(Map.of("modifyResult", modifyResult), HttpStatus.OK);
    
  }
  
  /**
   * 기존회원 비밀번호 변경
   * 
   * @author 심희수
   * @param request
   * @return 변경된 값(비밀번호)의 데이터를 전달
   */
  @Override
  public int modifyPw(HttpServletRequest request) {
    
    String pw = mySecurityUtils.getSHA256(request.getParameter("pw"));
    int userNo = Integer.parseInt(request.getParameter("userNo"));
    
    UserDto user = UserDto.builder()
                    .pw(pw)
                    .userNo(userNo)
                    .build();
    int modifyPwResult = userMapper.updateUserPw(user);
    
    return modifyPwResult;
  }
  
  /**
   * 기존회원 탈퇴
   * 기존회원 정보를 탈퇴회원에 추가한뒤,
   * 기존회원의 탈퇴를 진행한다.
   * 
   * @author 심희수
   * @param userNo 탈퇴할 회원번호
   */
  @Override
  public void leaveUser(HttpServletRequest request, HttpServletResponse response) {

    Optional<String> opt = Optional.ofNullable(request.getParameter("userNo"));
    int userNo = Integer.parseInt(opt.orElse("0"));
    
    UserDto user = userMapper.getUser(Map.of("userNo", userNo));
    int insertLeaveUserResult = userMapper.insertLeaveUser(user);
    int deleteUserResult = userMapper.deleteUser(user);
    
    try {
      
      response.setContentType("text/html; charset=UTF-8");
      PrintWriter out = response.getWriter();
      out.println("<script>");
      if(insertLeaveUserResult == 1 && deleteUserResult == 1) {
        out.println("alert('회원 탈퇴가 완료되었습니다. 탈퇴회원 목록에서 확인 가능합니다.')");
        out.println("location.href='" + request.getContextPath() + "/manage/leaveUserList.do'");  // 회원 탈퇴시킨 뒤 탈퇴회원 관리 목록으로 이동
      } else {
        out.println("alert('회원이 탈퇴되지 않았습니다.')");
      }
      out.println("</script>");
      out.flush();
      out.close();
      
    } catch (Exception e) {
      e.printStackTrace();
    }
    
  }
  
  /**
   * 찜 목록 
   */

  
  /**
   * 휴면회원 목록
   * MVC 페이징 처리
   * 
   * @author 심희수
   * @param request
   * @param model
   */
  @Transactional(readOnly=true)
  @Override
  public void loadInactiveList(HttpServletRequest request, Model model) {
    
    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    int page = Integer.parseInt(opt.orElse("1"));
    int total = manageMapper.getInactiveCount();
    int display = 20;
    
    myPageUtils.setPaging(page, total, display);
    
    Map<String, Object> map = Map.of("begin", myPageUtils.getBegin()
                                   , "end", myPageUtils.getEnd());
    
    List<InactiveUserDto> inactiveList = manageMapper.getInactiveList(map);
    
    model.addAttribute("inactiveList", inactiveList);
    model.addAttribute("paging", myPageUtils.getMvcPaging(request.getContextPath() + "/manage/inactiveList.do"));
    model.addAttribute("beginNo", total - (page - 1) * display);
    model.addAttribute("total", total);
    
  }
  
  /**
   * 휴면회원 검색
   * 
   * @author 심희수
   * @param request
   * @param model
   */
  @Transactional(readOnly=true)
  @Override
  public void loadSearchInactiveList(HttpServletRequest request, Model model) {
    
    String column = request.getParameter("column");
    String query = request.getParameter("query");
    
    Map<String, Object> map = new HashMap<>();
    map.put("column", column);
    map.put("query", query);
    
    int total = manageMapper.getSearchInactiveCount(map);
    
    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    int page = Integer.parseInt(opt.orElse("1"));
    int display = 20;
    
    myPageUtils.setPaging(page, total, display);
    
    map.put("begin", myPageUtils.getBegin());
    map.put("end", myPageUtils.getEnd());
    
    List<InactiveUserDto> inactiveList = manageMapper.getSearchInactive(map);
    
    model.addAttribute("inactiveList", inactiveList);
    model.addAttribute("paging", myPageUtils.getMvcPaging(request.getContextPath() + "/manage/searchInactiveList.do", "column=" + column + "&query" + query));
    model.addAttribute("beginNo", total - (page - 1) * display);
    model.addAttribute("total", total);
  }
  
  /**
   * 휴면회원 상세
   * 
   * @author 심희수
   * @param userNo 휴면회원번호
   * @return 휴면회원번호를 반환
   */
  @Transactional(readOnly=true)
  @Override
  public InactiveUserDto getInactiveUser(int userNo) {
    return manageMapper.getInactiveUser(userNo);
  }
  
  /**
   * 탈퇴회원 목록
   * MVC페이징 처리
   * @author 심희수
   * @param request
   * @param model
   */
  @Transactional(readOnly=true)
  @Override
  public void loadLeaveUserList(HttpServletRequest request, Model model) {
    
    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    int page = Integer.parseInt(opt.orElse("1"));
    int total = manageMapper.getLeaveUserCount();
    int display = 20;
    
    myPageUtils.setPaging(page, total, display);
    
    Map<String, Object> map = Map.of("begin", myPageUtils.getBegin()
                                   , "end", myPageUtils.getEnd());
    
    List<LeaveUserDto> leaveUserList = manageMapper.getLeaveUserList(map);
    
    model.addAttribute("leaveUserList", leaveUserList);
    model.addAttribute("paging", myPageUtils.getMvcPaging(request.getContextPath() + "/manage/leaveUserList.do"));
    model.addAttribute("beginNo", total - (page - 1) * display);
    model.addAttribute("total", total);
    
  }
  
  /**
   * 탈퇴 회원 검색
   * 
   * @author 심희수
   * @param request
   * @param model
   */
  @Transactional(readOnly=true)
  @Override
  public void loadSearchLeaveList(HttpServletRequest request, Model model) {

    String column = request.getParameter("column");
    String query = request.getParameter("query");
    
    Map<String, Object> map = new HashMap<>();
    map.put("column", column);
    map.put("query", query);
    
    int total = manageMapper.getSearchLeaveCount(map);
    
    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    int page = Integer.parseInt(opt.orElse("1"));
    
    int display = 20;
    
    myPageUtils.setPaging(page, total, display);
    
    map.put("begin", myPageUtils.getBegin());
    map.put("end", myPageUtils.getEnd());
    
    List<LeaveUserDto> leaveUserList = manageMapper.getSearchLeaveList(map);
    
    model.addAttribute("leaveUserList", leaveUserList);
    model.addAttribute("paging", myPageUtils.getMvcPaging(request.getContextPath() + "/manage/searchLeaveList.do", "column=" + column + "&query=" + query));
    model.addAttribute("beginNo", total - (page - 1) * display);
    model.addAttribute("total", total);
    
  }
  
  /**
   * 패키지 상품 목록
   * 
   * @author 심희수
   * @param request
   * @param model
   */
  @Transactional(readOnly=true)
  @Override
  public void loadPackageList(HttpServletRequest request, Model model) {
    
    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    int page = Integer.parseInt(opt.orElse("1"));
    int total = manageMapper.getPackageCount();
    int display = 20;
    
    myPageUtils.setPaging(page, total, display);
    
    Map<String, Object> map = Map.of("begin", myPageUtils.getBegin()
                                   , "end", myPageUtils.getEnd());
    
    List<PackageDto> packageList = manageMapper.getPackageList(map);
    List<RegionDto> regionList = manageMapper.getRegionList();

    model.addAttribute("packageList", packageList);
    model.addAttribute("regionList", regionList);
    model.addAttribute("paging", myPageUtils.getMvcPaging(request.getContextPath() + "/manage/productList.do"));
    model.addAttribute("beginNo", total - (page - 1) * display);
    model.addAttribute("total", total);
  }
  
  /**
   * 패키지여행 상품 검색
   * 
   * @author 심희수
   * @param request
   * @param model
   */
  @Transactional(readOnly=true)
  @Override
  public void loadSearchPackageProductList(HttpServletRequest request, Model model) {
    
    int regionNo = Integer.parseInt(request.getParameter("regionNo"));
    int status = Integer.parseInt(request.getParameter("status"));
    int recommendStatus = Integer.parseInt(request.getParameter("recommendStatus"));
    String column = request.getParameter("column");
    String query = request.getParameter("query");
    
    Map<String, Object> map = new HashMap<>();
    map.put("regionNo", regionNo);
    map.put("status", status);
    map.put("recommendStatus", recommendStatus);
    map.put("column", column);
    map.put("query", query);
    
    int total = manageMapper.getSearchPackageProducCount(map);
    
    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    int page = Integer.parseInt(opt.orElse("1"));
    int display = 20;
    
    myPageUtils.setPaging(page, total, display);
    
    map.put("begin", myPageUtils.getBegin());
    map.put("end", myPageUtils.getEnd());
    
    List<PackageDto> packageList = manageMapper.getSearchPackageProductList(map);
    List<RegionDto> regionList = manageMapper.getRegionList();
    
    model.addAttribute("packageList", packageList);
    model.addAttribute("regionList", regionList);
    model.addAttribute("paging", myPageUtils.getMvcPaging(request.getContextPath() + "/manage/packageProductSearch.do"
                                                        , "column=" + column + "&query=" + query + "&regionNo=" + regionNo + "&status=" + status + "&recommendStatus=" + recommendStatus));
    model.addAttribute("beginNo", total - (page - 1) * display);
    model.addAttribute("total", total);
    
  }
  
  /**
   * 호텔 상품 목록
   * 
   * @author 심희수
   * @param request
   * @param model
   */
  @Transactional(readOnly=true)
  @Override
  public void loadHotelList(HttpServletRequest request, Model model) {
    
    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    int page = Integer.parseInt(opt.orElse("1"));
    int total = manageMapper.getHotelCount();
    int display = 20;
    
    myPageUtils.setPaging(page, total, display);
    
    Map<String, Object> map = Map.of("begin", myPageUtils.getBegin()
                                   , "end", myPageUtils.getEnd());
    List<HotelDto> hotelList = manageMapper.getHotelList(map);
    
    List<RoompriceDto> roompriceList = manageMapper.getRoomPrice();
    List<RoomtypeDto> roomtypeList = manageMapper.getRoomType();
    List<RegionDto> regionList = manageMapper.getRegionList();
    
    model.addAttribute("hotelList", hotelList);
    model.addAttribute("roompriceList", roompriceList);
    model.addAttribute("roomtypeList", roomtypeList);
    model.addAttribute("regionList", regionList);
    model.addAttribute("paging", myPageUtils.getMvcPaging(request.getContextPath() + "/manage/hotelProductList.do"));
    model.addAttribute("beginNo", total - (page - 1) * display);
    model.addAttribute("total", total);
  }
  
  /**
   * 호텔 상품 검색
   * 
   * @author 심희수
   * @param request
   * @param model
   */
  @Transactional(readOnly=true)
  @Override
  public void loadSearchHotelProductList(HttpServletRequest request, Model model) {
    
    int regionNo = Integer.parseInt(request.getParameter("regionNo"));
    int status = Integer.parseInt(request.getParameter("status"));
    int recommendStatus = Integer.parseInt(request.getParameter("recommendStatus"));
    String column = request.getParameter("column");
    String query = request.getParameter("query");
    
    Map<String, Object> map = new HashMap<>();
    map.put("regionNo", regionNo);
    map.put("status", status);
    map.put("recommendStatus", recommendStatus);
    map.put("column", column);
    map.put("query", query);
    
    int total = manageMapper.getSearchHotelProductCount(map);
    
    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    int page = Integer.parseInt(opt.orElse("1"));
    int display = 20;
    
    myPageUtils.setPaging(page, total, display);
    
    map.put("begin", myPageUtils.getBegin());
    map.put("end", myPageUtils.getEnd());
    
    List<HotelDto> hotelList = manageMapper.getSearchHotelProductList(map);
    List<RoompriceDto> roompriceList = manageMapper.getRoomPrice();
    List<RoomtypeDto> roomtypeList = manageMapper.getRoomType();
    List<RegionDto> regionList = manageMapper.getRegionList();
    
    model.addAttribute("hotelList", hotelList);
    model.addAttribute("roompriceList", roompriceList);
    model.addAttribute("roomtypeList", roomtypeList);
    model.addAttribute("regionList", regionList);
    model.addAttribute("paging", myPageUtils.getMvcPaging(request.getContextPath() + "/manage/hotelProductSearch.do"
                                                        , "column=" + column + "&query=" + query + "&regionNo=" + regionNo + "&status=" + status + "&recommendStatus=" + recommendStatus));
    model.addAttribute("beginNo", total - (page - 1) * display);
    model.addAttribute("total", total);
    
  }
  
  
  /**
   * 호텔 객실 가격 변경
   * 
   * @author 심희수
   * @param request
   * @return 변경된 데이터 수 반환
   */
  @Override
  public ResponseEntity<Map<String, Object>> modifyRoomPrice(HttpServletRequest request) {
    
    int hotelNo = Integer.parseInt(request.getParameter("hotelNo"));
    int roomNo = Integer.parseInt(request.getParameter("roomNo"));
    String ssDate = request.getParameter("ssDate");
    String seDate = request.getParameter("seDate");
    String jsDate = request.getParameter("jsDate");
    String jeDate = request.getParameter("jeDate");
    String bsDate = request.getParameter("bsDate");
    String beDate = request.getParameter("beDate");
    int sungPrice = Integer.parseInt(request.getParameter("sungPrice"));
    int junPrice = Integer.parseInt(request.getParameter("junPrice"));
    int biPrice = Integer.parseInt(request.getParameter("biPrice"));
    
    RoompriceDto roomprice = RoompriceDto.builder()
                                .hotelNo(hotelNo)
                                .roomNo(roomNo)
                                .seDate(seDate)
                                .ssDate(ssDate)
                                .sungPrice(sungPrice)
                                .jsDate(jsDate)
                                .jeDate(jeDate)
                                .junPrice(junPrice)
                                .bsDate(bsDate)
                                .beDate(beDate)
                                .biPrice(biPrice)
                                .build();
    
    int modifyPriceResult = manageMapper.updateRoomPrice(roomprice);
    return new ResponseEntity<>(Map.of("modifyPriceResult", modifyPriceResult), HttpStatus.OK);
  }
  
  /**
   * 패키지 추천 여부 변경
   * 
   * @author 심희수
   * @param request
   * @return 변경된 데이터 수 반환
   */
  @Override
  public ResponseEntity<Map<String, Object>> modifyPackageRecommend(HttpServletRequest request) {
    
    int recommendStatus = Integer.parseInt(request.getParameter("recommendStatus"));
    int packageNo = Integer.parseInt(request.getParameter("packageNo"));
    
    PackageDto packageDto = PackageDto.builder()
                              .recommendStatus(recommendStatus)
                              .packageNo(packageNo)
                              .build();
    
    int modifyRecommendResult = manageMapper.updatePackageRecommend(packageDto);
    
    return new ResponseEntity<>(Map.of("modifyRecommendResult", modifyRecommendResult), HttpStatus.OK);
  }
  
  /**
   * 호텔 추천 여부 변경
   * 
   * @author 심희수
   * @param request
   * @return 변경된 데이터 수 반환
   */
  @Override
  public ResponseEntity<Map<String, Object>> modifyHotelRecommend(HttpServletRequest request) {
    
    int recommendStatus = Integer.parseInt(request.getParameter("recommendStatus"));
    int hotelNo = Integer.parseInt(request.getParameter("hotelNo"));
    
    HotelDto hotel = HotelDto.builder()
        .recommendStatus(recommendStatus)
        .hotelNo(hotelNo)
        .build();
    
    int modifyRecommendResult = manageMapper.updateHotelRecommend(hotel);
    
    return new ResponseEntity<>(Map.of("modifyRecommendResult", modifyRecommendResult), HttpStatus.OK);
  }
  
  /**
   * 예약 관리 목록
   * 
   * @author 심희수
   * @param request
   * @param model
   */
  @Transactional(readOnly=true)
  @Override
  public void loadReserveList(HttpServletRequest request, Model model) {
    
    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    int page = Integer.parseInt(opt.orElse("1"));
    int total = manageMapper.getReserveCount();
    int display = 20;
    
    myPageUtils.setPaging(page, total, display);
    
    Map<String, Object> map = Map.of("begin", myPageUtils.getBegin()
                                   , "end", myPageUtils.getEnd());
    
    List<ReserveDto> reserveList = manageMapper.getReserveList(map);
    
    model.addAttribute("reserveList", reserveList);
    model.addAttribute("paging", myPageUtils.getMvcPaging(request.getContextPath() + "/manage/reserveList.do"));
    model.addAttribute("beginNo", total - (page - 1) * display);
    model.addAttribute("total", total);
    
  }
  
  /**
   * 예약 검색
   * 
   * @author 심희수
   * @param request
   * @param model
   */
  @Transactional(readOnly=true)
  @Override
  public void loadSearchReserveList(HttpServletRequest request, Model model) {

    String columnGubun = request.getParameter("columnGubun");
    int reserveStatus = Integer.parseInt(request.getParameter("reserveStatus"));
    String columnRe = request.getParameter("columnRe");
    String queryRe = request.getParameter("queryRe");
    String columnPro = request.getParameter("columnPro");
    String queryPro = request.getParameter("queryPro");
    String columnProName = request.getParameter("columnProName");
    String queryProName = request.getParameter("queryProName");
    
    Map<String, Object> map = new HashMap<>();
    map.put("columnGubun", columnGubun);
    map.put("reserveStatus", reserveStatus);
    map.put("columnRe", columnRe);
    map.put("queryRe", queryRe);
    map.put("columnPro", columnPro);
    map.put("queryPro", queryPro);
    map.put("columnProName", columnProName);
    map.put("queryProName", queryProName);
    
    int total = manageMapper.getSearchReserveCount(map);
    
    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    int page = Integer.parseInt(opt.orElse("1"));
    int display = 20;
    
    myPageUtils.setPaging(page, total, display);
    
    map.put("begin", myPageUtils.getBegin());
    map.put("end", myPageUtils.getEnd());
    
    List<ReserveDto> reserveList = manageMapper.getSearchReserveList(map);
    
    model.addAttribute("reserveList", reserveList);
    model.addAttribute("paging", myPageUtils.getMvcPaging(request.getContextPath() + "/manage/searchReserve.do"
                                                        , "columnGubun=" + columnGubun
                                                        + "&reserveStatus=" + reserveStatus
                                                        + "&columnRe=" + columnRe
                                                        + "&queryRe=" + queryRe
                                                        + "&columnPro=" + columnPro
                                                        + "&queryPro=" + queryPro
                                                        + "&columnProName=" + columnProName
                                                        + "&queryProName=" + queryProName));
    model.addAttribute("beginNo", total - (page - 1) * display);
    model.addAttribute("total", total);
  }
  
  /**
   * 전체 리뷰 목록
   * 
   * @author 심희수
   * @param request
   * @param model
   */
  @Transactional(readOnly=true)
  @Override
  public void loadReviewList(HttpServletRequest request, Model model) {
    
    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    int page = Integer.parseInt(opt.orElse("1"));
    int total = manageMapper.getReviewCount();
    int display = 20;
    
    myPageUtils.setPaging(page, total, display);
    
    Map<String, Object> map = new HashMap<>();
    map.put("begin", myPageUtils.getBegin());
    map.put("end", myPageUtils.getEnd());
    
    List<ReviewDto> reviewList = manageMapper.getReviewList(map);
    
    model.addAttribute("reviewList", reviewList);
    model.addAttribute("paging", myPageUtils.getMvcPaging(request.getContextPath() + "/manage/reviewList.do"));
    model.addAttribute("beginNo", total - (page - 1) * display);
    model.addAttribute("total", total);
  }
  
  /**
   * 리뷰 검색
   * 
   * @author 심희수
   * @param request
   * @param model
   */
  @Transactional(readOnly=true)
  @Override
  public void loadSearchReviewList(HttpServletRequest request, Model model) {
    
    String columnGubun = request.getParameter("columnGubun");
    String column = request.getParameter("column");
    String query = request.getParameter("query");
    
    Map<String, Object> map = new HashMap<>();
    map.put("columnGubun", columnGubun);
    map.put("column", column);
    map.put("query", query);
    
    int total = manageMapper.getSearchReviewCount(map);
    
    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    int page = Integer.parseInt(opt.orElse("1"));
    int display = 20;
    
    myPageUtils.setPaging(page, total, display);
    
    map.put("begin", myPageUtils.getBegin());
    map.put("end", myPageUtils.getEnd());
    
    List<ReviewDto> reviewList = manageMapper.getSearchReviewList(map);
    
    model.addAttribute("reviewList", reviewList);
    model.addAttribute("paging", myPageUtils.getMvcPaging(request.getContextPath() + "/manage/searchReview.do", "column=" + column + "&query=" + query + "&columnGubun=" + columnGubun));
    model.addAttribute("beginNo", total - (page - 1) * display);
    model.addAttribute("total", total);
  }
  
  /**
   * 리뷰 삭제
   * 
   * @author 심희수
   * @param reviewNo 삭제할 리뷰의 번호 전달
   * @return 삭제하는 리뷰 정보 반환
   */
  @Override
  public int removeReview(int reviewNo) {
    return manageMapper.deleteReview(reviewNo);
  }
  
}















