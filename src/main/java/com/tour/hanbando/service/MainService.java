package com.tour.hanbando.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface MainService {
  public Map<String, Object> SearchPackageList(HttpServletRequest request);
  public Map<String, Object> SearchHotelList(HttpServletRequest request);
  public Map<String, Object> getBestPackage();
  public Map<String, Object> getThemePackage();
  public void bannerList(Model model);
  public int addBannerImage(MultipartHttpServletRequest multipartRequest) throws Exception;
  
}
