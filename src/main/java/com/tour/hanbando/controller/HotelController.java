package com.tour.hanbando.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tour.hanbando.service.HotelService;

import lombok.RequiredArgsConstructor;
import retrofit2.http.GET;

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
    hotelService.makeHotelNo(model);
    return "hotel/write";
  }
  @ResponseBody
  @GetMapping("roomList.do")
  public void roomList(HttpServletRequest request, Model model){
    
    return ;
  }
  
  
  @PostMapping("addHotel.do")
  public String writeHotel(MultipartHttpServletRequest multipartHttpServletRequest, RedirectAttributes redirectAttributes) {
    return "redirect:/hotel/list.do";
  }
  
  @GetMapping("addRoom.form")
  public String HotelRoom(HttpServletRequest request, Model model) {
   model.addAttribute("hotelNo", request.getAttribute("hotelNo"));
    return "hotel/hotelRoom";  
  }
  
  @PostMapping("addRoom.do")
  public void writeRoom(MultipartHttpServletRequest multipartRequest, Model model) throws Exception {
    
    System.out.println("@@@@@@@@@@@@뭐가 널인데" + multipartRequest.getParameter("bsDate"));
    
    
    boolean addResult = hotelService.writeRoom(multipartRequest);
    model.addAttribute("addResult", addResult);
    
  }
  
}
