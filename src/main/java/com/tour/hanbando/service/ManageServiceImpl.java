package com.tour.hanbando.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.tour.hanbando.dao.ManageMapper;
import com.tour.hanbando.dao.UserMapper;
import com.tour.hanbando.dto.InactiveUserDto;
import com.tour.hanbando.dto.LeaveUserDto;
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
   * 기존회원 탈퇴
   * 기존회원 정보를 탈퇴회원에 추가한뒤,
   * 기존회원의 탈퇴를 진행한다.
   * 
   * @author 심희수
   * @param userNo 탈퇴할 회원번호
   * @return 탈퇴된 회원의 데이터 수를 반환
   */
  @Override
  public int leaveUser(int userNo) {
    UserDto user = userMapper.getUser(Map.of("userNo", userNo));
    int addLeaveUserResult = userMapper.insertLeaveUser(user);
    int leaveUserResult = userMapper.deleteUser(user);
    if(addLeaveUserResult == 1 && leaveUserResult ==1) {
      return leaveUserResult;
    } else {
      return 0;
    }
  }
  
  
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
   * @return 탈퇴한 회원 리스트, 페이징 정보, 총 탈퇴 회원수를 반환
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
   * @return 검색된 탈퇴 회원 목록, 페이징 정보, 검색된 총 탈퇴 회원수를 반환
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
  
}















