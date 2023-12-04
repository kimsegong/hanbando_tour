package com.tour.hanbando.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    
    return hotelService.getHotelList(request);
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
  public String write(Model model) {
    hotelService.regionList(model);
    return "hotel/write";
  }
  
  @PostMapping("addHotel.do")
  public String writeHotel(MultipartHttpServletRequest multipartHttpServletRequest, RedirectAttributes redirectAttributes) {
    return "redirect:/hotel/list.do";
  }
  
  
  @PostMapping("addRoom.do")
  public void writeHotelRoom(MultipartHttpServletRequest multipartHttpServletRequest) {
      
  }
  
  
}
