package com.tour.hanbando.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tour.hanbando.service.HotelService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/hotel")
@RequiredArgsConstructor
@Controller
public class HotelController {
  private final HotelService hotelService;
  
 /*************************** 리스트 ***************************************************/
  
  @GetMapping("list.do")
  public String hotelList() {
    return "hotel/list";
  }
  
  @ResponseBody
  @GetMapping("getList.do")
  public Map<String, Object> getHotelist(HttpServletRequest request){
    System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"+hotelService.getHotelList(request));
    return hotelService.getHotelList(request);
  }
  
  
  
  @GetMapping("sortedHotelList.do")
  public Map<String, Object> getSortedHotelList(HttpServletRequest request){
    return ;
  }
  
  @GetMapping("increseHit.do")
  public String increseHit(@RequestParam(value="hotelNo", required=false, defaultValue="0") int hotelNo) {
    int increseResult = hotelService.increseHit(hotelNo);
    if(increseResult == 1) {
      return "redirect:/hotel/detail.do?hotelNo=" + hotelNo;
    } else {
      return "redirect:/hotel/list.do";
    }
  }
  
  @GetMapping("detail.do")
  public String hotelDetail(
      @RequestParam(value = "packageNo", required = false, defaultValue = "0") int hotelNo, 
      HttpServletRequest request, Model model) {
    
   return "hotel/detail";
  } 
  
  /*************************** 작성 ***************************************************/  
  @GetMapping("write.form")
  public String write() {
    return "hotel/write";
  }
}
