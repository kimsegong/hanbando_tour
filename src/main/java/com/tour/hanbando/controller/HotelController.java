package com.tour.hanbando.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tour.hanbando.service.HotelService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/hotel")
@RequiredArgsConstructor
@Controller
public class HotelController {
  private final HotelService hotelService;
  
  
}
