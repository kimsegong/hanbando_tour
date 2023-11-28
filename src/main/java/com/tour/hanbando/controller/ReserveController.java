package com.tour.hanbando.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tour.hanbando.dto.ReserveDto;
import com.tour.hanbando.service.ReserveService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/reserve")
@RequiredArgsConstructor
@Controller
public class ReserveController {

  private final ReserveService reserveService;
  
  @GetMapping("/reserveList.do")
  public String list(HttpServletRequest request, Model model) {
    if(request.getParameter("userNo") != null) { // userNo가 파라미터로 넘어오는 경우 
      reserveService.loadReserveListByUser(request, model);
    } else { // 파라미터가 없는 경우
      reserveService.loadReserveList(request, model);
    }
    return "reserve/list";
  }
  
  @RequestMapping("/write.form")
  public String write() {
    return "reserve/write";
  }
  
  @GetMapping("/detail.do")
  public String detail(@RequestParam(value="reserveNo", required = false, defaultValue = "0") int reserveNo, Model model) {
    ReserveDto reserve = reserveService.loadReserve(reserveNo);
    model.addAttribute("reserve", reserve);
    return "reserve/detail";
  }
  
  @RequestMapping("/edit.form")
  public String edit() {
    return "reserve/edit";
  }
 
  
  @ResponseBody
  @GetMapping(value="/getTouristInfo.do", produces="application/json")
  public Map<String, Object> getTourists(HttpServletRequest request) {
    return reserveService.loadTourists(request);
  }
  
  
  
  
}
