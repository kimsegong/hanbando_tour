package com.tour.hanbando.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

public interface HotelService {
  /********************리스트**************************/
  public Map<String, Object> getHotelList(HttpServletRequest request);
  public int increseHit(int hotelNo);
  public void regionList(Model model);
  public void hotelRoomList(Model model);
  public void makeHotelNo(Model model);
  public void makeRoomNo(Model model);
}
