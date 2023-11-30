package com.tour.hanbando.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.tour.hanbando.dao.ReserveMapper;
import com.tour.hanbando.dto.PackageDto;
import com.tour.hanbando.dto.ReserveDto;
import com.tour.hanbando.dto.TouristDto;
import com.tour.hanbando.dto.UserDto;
import com.tour.hanbando.util.MyPageUtils;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class ReserveServiceImpl implements ReserveService {

  private final ReserveMapper reserveMapper;
  private final MyPageUtils myPageUtils;
  
  @Override
  public Map<String, Object> addReserve(HttpServletRequest request) throws Exception {
    /*
    String oldFormat = "MM/dd/yyyy";
    String newFormat = "yyyy/MM/dd";

    String resStart = req.getParameter("resStart"); 
    
    SimpleDateFormat sdf = new SimpleDateFormat(oldFormat);
    Date d = sdf.parse(resStart); 
    sdf.applyPattern(newFormat);
    String reserveStart = sdf.format(d); // 20231115
    
    ReserveDto reserve =
    
    */
    String requestedTerm = null;
    if(request.getParameter("reqTerm") == null) {
      requestedTerm = "";
    } else {
      requestedTerm = request.getParameter("reqTerm");
    }
    
    int agree = 0;
    String requiredA = request.getParameter("requiredA");
    String marketingA = request.getParameter("marketingA");
    if(marketingA != null && requiredA.equals("0")) {
      agree = 1;
    }
    
    String departureLoc = request.getParameter("departureLoc");
    int reserveStatus = Integer.parseInt(request.getParameter("resStatus"));
//    String reserveStart = "null";
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
//                            .reserveStart(reserveStart)
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
            .name(names[i])
            .birthDate(birthDate)
            .gender(genders[i])
            .mobile(mobiles[i])
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
    model.addAttribute("paging", myPageUtils.getMvcPaging(request.getContextPath() + "/reserve/reserveList.do", request.getParameter("userNo")));
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
  
  
  
}
