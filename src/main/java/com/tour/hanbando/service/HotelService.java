package com.tour.hanbando.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface HotelService {
  public Map<String, Object> getHotelList(HttpServletRequest request);
}
