package com.tour.hanbando.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tour.hanbando.service.HotelService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/hotel")
@RequiredArgsConstructor
@Controller
public class HotelController {
  private final HotelService hotelService;
  
  @GetMapping("/bannerList.do")
  public String banner() {
    return "manage/bannerList";
  }
  
  @GetMapping("list.do")
  public String hotelList() {
    return "hotel/list";
  }
}
