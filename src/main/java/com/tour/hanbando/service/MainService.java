package com.tour.hanbando.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

public interface MainService {
  public Map<String, Object> SearchPackageList(HttpServletRequest request);
  public Map<String, Object> SearchHotelList(HttpServletRequest request);
  public Map<String, Object> getBestPackage();
  public Map<String, Object> getThemePackage();
}
