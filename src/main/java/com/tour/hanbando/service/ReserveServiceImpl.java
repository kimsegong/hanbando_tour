package com.tour.hanbando.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tour.hanbando.dao.ReserveMapper;
import com.tour.hanbando.dto.PackageDto;
import com.tour.hanbando.dto.PaymentDto;
import com.tour.hanbando.dto.ReserveDto;
import com.tour.hanbando.dto.TouristDto;
import com.tour.hanbando.dto.UserDto;
import com.tour.hanbando.util.MyPageUtils;
import com.tour.hanbando.util.MySecurityUtils;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class ReserveServiceImpl implements ReserveService {

  private final ReserveMapper reserveMapper;
  private final MyPageUtils myPageUtils;
  private final MySecurityUtils mySecurityUtils;
  
  @Override
  public Map<String, Object> addReserve(HttpServletRequest request) throws Exception {

    String requestedTerm = null;
    if(request.getParameter("reqTerm") == null) {
      requestedTerm = "";
    } else {
      requestedTerm = mySecurityUtils.preventXSS(request.getParameter("reqTerm"));
    }
    
    int agree = 0;
    String requiredA = request.getParameter("requiredA");
    String marketingA = request.getParameter("marketingA");
    if(marketingA != null && requiredA.equals("0")) {
      agree = 1;
    }
    
    String departureLoc = request.getParameter("departureLoc");
    int reserveStatus = Integer.parseInt(request.getParameter("resStatus"));
    String reserveStart = request.getParameter("resStart").replace("-", "/");
//    String reserveFinish = "null";
    int reservePerson = Integer.parseInt(request.getParameter("reservePerson"));
    int reservePrice = Integer.parseInt(request.getParameter("totalReservePrice"));
    int userNo = Integer.parseInt(request.getParameter("userNo"));
    int packageNo = Integer.parseInt(request.getParameter("packageNo"));
    
    ReserveDto reserve = ReserveDto.builder()
                            .requestedTerm(requestedTerm)
                            .agree(agree)
                            .departureLoc(departureLoc)
                            .reserveStatus(reserveStatus)
                            .reserveStart(reserveStart)
//                            .reserveFinish(reserveFinish)
                            .reservePerson(reservePerson)
                            .reservePrice(reservePrice)
                            .userDto(UserDto.builder()
                                          .userNo(userNo)
                                          .build())
                            .packageDto(PackageDto.builder()
                                          .packageNo(packageNo)
                                          .build())
                            .build();
    
    int addReserveResult = reserveMapper.insertReserve(reserve);
    
    return Map.of("addReserveResult", addReserveResult, "reserveNo", reserve.getReserveNo());
  }
  
  @Override
  public int addTourist(HttpServletRequest request) throws Exception {

    String[] names = request.getParameterValues("touristName");
    String[] bDates = request.getParameterValues("birthDate");
    String[] genders = request.getParameterValues("gender");
    String[] mobiles = request.getParameterValues("tourMobile");
    String[] ageCases = request.getParameterValues("ageCase");
    int reserveNo = Integer.parseInt(request.getParameter("reserveNo"));
    
    int result = 0;
    for (int i = 0; i < names.length; i++) {
        
        int ageCase = Integer.parseInt(ageCases[i]);
        String birthDate = bDates[i].replace("-", "/");
        TouristDto tourist = TouristDto.builder()
            .name(mySecurityUtils.preventXSS(names[i]))
            .birthDate(birthDate)
            .gender(genders[i])
            .mobile(mySecurityUtils.preventXSS(mobiles[i]))
            .ageCase(ageCase)
            .reserveDto(ReserveDto.builder()
                .reserveNo(reserveNo)
                .build())
            .build();
        System.out.println(tourist);
        result += reserveMapper.insertTourist(tourist);
    }
    return result;
  }
  
  @Override
  public Map<String, Object> addPayment(HttpServletRequest request, PaymentDto payment) {
    String paidAt = null;
    String errorMsg = null;
    if(payment.getErrorMsg() == null) {
      errorMsg = "";
    } else {
      errorMsg = payment.getErrorMsg();
    }
    String impUid = payment.getImpUid();
    String payYn = payment.getPayYn();
    String payMethod = payment.getPayMethod();
    int paidAmount = payment.getPaidAmount();
    
    if(payment.getPaidAt() != null) {
      String paidAtTimestamp = payment.getPaidAt();
      long seconds = Long.parseLong(paidAtTimestamp);
      LocalDateTime dateTime = Instant.ofEpochSecond(seconds).atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime();
      int year = dateTime.getYear();
      int month = dateTime.getMonthValue();
      int day = dateTime.getDayOfMonth();
      int hour = dateTime.getHour();
      int minute = dateTime.getMinute();
      int second = dateTime.getSecond();
      paidAt = year + "/" + month + "/" + day + " " + hour + ":" + minute + ":" + second;
    }
    
    
    String buyerName = payment.getBuyerName();
    String buyerEmail = payment.getBuyerEmail();
    String payStatus = payment.getPayStatus();
    String merchantUid = payment.getMerchantUid();
    int reserveNo = payment.getReserveDto().getReserveNo();
    System.out.println("===============================PaymentDto build 준비완료================================");
    
    PaymentDto paymentDto = PaymentDto.builder()
                            .impUid(impUid)
                            .payYn(payYn)
                            .payMethod(payMethod)
                            .paidAmount(paidAmount)
                            .paidAt(paidAt)
                            .buyerName(buyerName)
                            .buyerEmail(buyerEmail)
                            .errorMsg(errorMsg)
                            .payStatus(payStatus)
                            .merchantUid(merchantUid)
                            .reserveDto(ReserveDto.builder()
                                                .reserveNo(reserveNo)
                                                .build())
                            .build();
    
    int addPaymentResult = reserveMapper.insertPayment(paymentDto);
    
    return Map.of("addPaymentResult", addPaymentResult);
  }
  
  @Transactional(readOnly=true)
  @Override
  public void loadReserveList(HttpServletRequest request, Model model) {
    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    int page = Integer.parseInt(opt.orElse("1"));
    int total = reserveMapper.getReserveCount();
    int display = 10;
    myPageUtils.setPaging(page, total, display);
    
    Map<String, Object> map = Map.of("begin", myPageUtils.getBegin()
                                   , "end", myPageUtils.getEnd());
    
    List<ReserveDto> reserveList = reserveMapper.getReserveList(map);
    
    model.addAttribute("reserveList", reserveList);
    model.addAttribute("paging", myPageUtils.getMvcPaging(request.getContextPath() + "/reserve/reserveList.do"));
    model.addAttribute("beginNo", total - (page - 1) * display);
  }

  @Transactional(readOnly=true)
  @Override
  public void loadReserveListByUser(HttpServletRequest request, Model model) {
    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    int page = Integer.parseInt(opt.orElse("1"));
    int display = 10;
    int userNo = Integer.parseInt(request.getParameter("userNo"));
    int total = reserveMapper.getReserveCountByUserNo(userNo);
    
    myPageUtils.setPaging(page, total, display);
    
    Map<String, Object> map = Map.of("begin", myPageUtils.getBegin()
                                   , "end", myPageUtils.getEnd() 
                                   , "userNo", userNo);
    
    List<ReserveDto> reserveList = reserveMapper.getReserveListByUser(map);
    
    model.addAttribute("reserveList", reserveList);
    String params = "userNo=" + request.getParameter("userNo");
    model.addAttribute("paging", myPageUtils.getMvcPaging(request.getContextPath() + "/reserve/reserveList.do", params));
    model.addAttribute("beginNo", total - (page - 1) * display);
  }
  
  @Transactional(readOnly=true)
  @Override
  public void loadReserveHotelListByUser(HttpServletRequest request, Model model) {
    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    int page = Integer.parseInt(opt.orElse("1"));
    int display = 10;
    int userNo = Integer.parseInt(request.getParameter("userNo"));
    int total = reserveMapper.getReserveHotelCountByUserNo(userNo);
    
    myPageUtils.setPaging(page, total, display);
    
    Map<String, Object> map = Map.of("begin", myPageUtils.getBegin()
                                   , "end", myPageUtils.getEnd() 
                                   , "userNo", userNo);
    
    List<ReserveDto> reserveHotelList = reserveMapper.getReserveHotelListByUser(map);
    
    model.addAttribute("reserveHotelList", reserveHotelList);
    String params = "userNo=" + request.getParameter("userNo");
    model.addAttribute("paging", myPageUtils.getMvcPaging(request.getContextPath() + "/reserve/reserveList.do", params));
    model.addAttribute("beginNo", total - (page - 1) * display);
  }
  
  
  @Transactional(readOnly=true)
  @Override
  public ReserveDto loadReserve(int reserveNo) {
    return reserveMapper.getReserve(reserveNo);
  }
  
  @Transactional(readOnly=true)
  @Override
  public Map<String, Object> loadTourists(HttpServletRequest request) {
    int reserveNo = Integer.parseInt(request.getParameter("reserveNo"));
    List<TouristDto> tourists = reserveMapper.getTourists(reserveNo);
    return Map.of("tourists", tourists);
  }
  
  @Transactional(readOnly=true)
  @Override
  public ReserveDto loadReserveHotel(int reserveNo) {
    return reserveMapper.getReserveHotel(reserveNo);
  }
  
  
  @Override
  public PaymentDto loadPaymentByReserveNo(int reserveNo) {
    return reserveMapper.getPaymentBy(Map.of("reserveNo", reserveNo));
  }
  
  @Override
  public Map<String, Object> loadPaymentByMerchantUid(HttpServletRequest request, PaymentDto payment) {
    String accessToken = getAccessToken(null, null);
    String merchantUid = payment.getMerchantUid();
    int cancelAmount = payment.getCancelAmount();
    PaymentDto payinfo = reserveMapper.getPaymentBy(Map.of("merchantUid", merchantUid));
    int cancelableAmount = (payinfo.getPaidAmount() - cancelAmount); 
    if(cancelableAmount < 0) {
      return Map.of("payInfo", HttpStatus.BAD_REQUEST);
    }
    return Map.of("payInfo", payinfo);
  }
  
  
  
  @Override
  public int modifyReserve(HttpServletRequest request) {
    int reserveNo = Integer.parseInt(request.getParameter("reserveNo"));
    String requestedTerm = request.getParameter("reqTerm");
    String departureLoc = request.getParameter("departureLoc");
    
    ReserveDto reserve = ReserveDto.builder()
                                   .requestedTerm(requestedTerm)
                                   .departureLoc(departureLoc)
                                   .reserveNo(reserveNo)
                                   .build();
    
    int modifyResult = reserveMapper.updateReserve(reserve);
    return modifyResult;
  }
  
  @Override
  public int removeReserve(HttpServletRequest request) {
    int reserveNo = Integer.parseInt(request.getParameter("reserveNo"));
    int removeResult = reserveMapper.deleteReserve(reserveNo);
    return removeResult;
  }
  
  
  @Override
  public Map<String, Object> modifyReserveStatusByPayStatus(Map<String, String> payload, HttpServletRequest request, RedirectAttributes redirectAttributes) {
    String reserveNoStr = payload.get("reserveNo");
    String payStatus = payload.get("payStatus");
    int reserveNo = Integer.parseInt(reserveNoStr);
    Map<String, Object> map = Map.of("reserveNo", reserveNo, "payStatus", payStatus);
    int modifyResStatusResult = reserveMapper.updateReserveStatus(map);
    return Map.of("modifyResStatusResult", modifyResStatusResult);
  }
  
  
  
  
  public String getAccessToken(String apiKey, String apiSecret) {
    String urlStr = "https://api.iamport.kr/users/getToken";
    String params = "{\"imp_key\": \"" + apiKey + "\", \"imp_secret\": \"" + apiSecret + "\"}";
    
    try {
      URL url = new URL(urlStr);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      
      conn.setRequestMethod("POST");
      conn.setRequestProperty("Content-Type", "application/json");
      conn.setDoOutput(true);
      
      OutputStream os = conn.getOutputStream();
      os.write(params.getBytes());
      os.flush();
      os.close();
      
      int responseCode = conn.getResponseCode();
      BufferedReader br;
      if(responseCode == 200) { // 정상 호출
        br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      } else {  // 에러 발생
        br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
      }
      
      String inputLine;
      StringBuffer response = new StringBuffer();
      while ((inputLine = br.readLine()) != null) {
        response.append(inputLine);
      }
      br.close();
      
      return response.toString();
      
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
  
  
  
  
  
  
  
  
  
}
