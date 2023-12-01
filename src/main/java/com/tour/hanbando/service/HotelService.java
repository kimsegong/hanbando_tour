package com.tour.hanbando.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface HotelService {
  /********************리스트**************************/
  public Map<String, Object> getHotelList(HttpServletRequest request);
  public Map<String, Object> getSortedHotelList(HttpServletRequest request);
  public int increseHit(int hotelNo);
}
