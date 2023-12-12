package com.tour.hanbando.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tour.hanbando.dto.ReserveDto;
import com.tour.hanbando.dto.RoomtypeDto;
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
    System.out.println(hotelService.getHotelList(request));
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
  /*************************** 상세 ***************************************************/  
  @GetMapping("detail.do")
  public String hotelDetail(@RequestParam(value = "hotelNo", required = false, defaultValue = "0") int hotelNo, 
                                HttpServletRequest request, Model model) {
   List<ReserveDto> reserve = hotelService.getReserveUser(hotelNo);
   model.addAttribute("reserve", reserve); 
   hotelService.hoteDetail(request, hotelNo, model); 
    
   return "hotel/detail";
  } 
  
  @ResponseBody
  @PostMapping(value="/addReview.do", produces="application/json")
  public Map<String, Object> addReview(HttpServletRequest request) {
    return hotelService.addReview(request);
  }
  
  @ResponseBody
  @GetMapping(value="/reviewList.do", produces="application/json")
  public Map<String, Object> reviewList(HttpServletRequest request){
    return hotelService.loadReviewList(request);
  }
  
  @ResponseBody
  @PostMapping(value="/getAverageRating.do", produces="application/json")
  public Map<String, Object> starAverage(@RequestParam(value="hotelNo", required=false, defaultValue="0") int hotelNo) {
      Map<String, Object> response = new HashMap<>();

      try {
          double averageRating = hotelService.getAverageRating(hotelNo);
          response.put("success", true);
          response.put("averageRating", averageRating);
      } catch (Exception e) {
          response.put("success", false);
          response.put("error", "Failed to get average rating.");
          e.printStackTrace();
      }
      return response;
  }
  
  @ResponseBody
  @PostMapping(value="/removeReview.do", produces="application/json")
  public Map<String, Object> removeReview(@RequestParam(value="reviewNo", required=false, defaultValue="0") int reviewNo) {
    return hotelService.removeReview(reviewNo);
  }
  
  @ResponseBody
  @PostMapping("/heart.do")
  public void getHeart (HttpServletRequest request, Model model) {
    int heartStatus = hotelService.getHeart(request);
    model.addAttribute("heart", heartStatus);
  }
  
  @ResponseBody
  @PostMapping("/finalPrice.do")
  public Map<String, Object> getFinalPrice(HttpServletRequest request) {
    return hotelService.getFinalPrice(request);  
  }
  
  /*************************** 작성 ***************************************************/  
  @GetMapping("write.form")
  public String write(Model model) {
    hotelService.regionList(model);
    hotelService.makeHotelNo(model);
    return "hotel/write";
  }
  @ResponseBody
  @GetMapping(value="roomList.do", produces = "application/json")
  public Map<String, Object> roomList(HttpServletRequest request){
    Map<String, Object> map = hotelService.hotelRoomList(request);
    System.out.println(map);
    return map;
  }
  
  @PostMapping("addHotel.do")
  public String writeHotel(MultipartHttpServletRequest multipartRequest, RedirectAttributes redirectAttributes) throws Exception {
   int hotelResult = hotelService.writeHotel(multipartRequest) ? 1 : 0;
    
    redirectAttributes.addFlashAttribute("hotelResult", redirectAttributes); 
    return "redirect:/hotel/list.do";
  }
  
  @GetMapping("addRoom.form")
  public String HotelRoom(@RequestParam(value="hotelNo", required=false, defaultValue="0")int hotelNo, Model model) {
   model.addAttribute("hotelNo", hotelNo);
    return "hotel/hotelRoom";  
  }
  
  @ResponseBody
  @PostMapping("addHotelRoom.do")
  public int writeRoom(MultipartHttpServletRequest multipartRequest, 
                        @RequestParam("files") List<MultipartFile> files) throws Exception {
    
    int addResult = hotelService.writeRoom(multipartRequest, files) ? 1 : 0;
    
    return addResult;
  }
  
  
  /*************************** 삭제 ***************************************************/  

  
  @PostMapping("remove.do")
  public String remove(@RequestParam(value="hotelNo", required=false, defaultValue="0") int hotelNo
                     , RedirectAttributes redirectAttributes) {
    int removeResult = hotelService.removehotel(hotelNo);
    redirectAttributes.addFlashAttribute("removeResult", removeResult);
    return "redirect:/hotel/list.do";
  }
  
  /*************************** 수정 ***************************************************/  
//  @PostMapping("/modifyBlog.do")
//  public String modifyBlog(HttpServletRequest request, RedirectAttributes redirectAttributes) {
//    int modifyResult = hotelService.modifyHotel(request);
//    redirectAttributes.addFlashAttribute("modifyResult", modifyResult);
//    return "redirect:/hotel/detail.do?hotelNo=" + request.getParameter("hotelNo");
//  }
   
  
  
}
