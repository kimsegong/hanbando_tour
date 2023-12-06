package com.tour.hanbando.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface HotelService {
  /********************리스트**************************/
  public Map<String, Object> getHotelList(HttpServletRequest request);
  public int increseHit(int hotelNo);
  public void regionList(Model model);
  public void hotelRoomList(HttpServletRequest request, Model model);
  public void makeHotelNo(Model model);
  public boolean writeRoom(MultipartHttpServletRequest multipartRequest, List<MultipartFile> files) throws Exception;
  public boolean writeHotel(MultipartHttpServletRequest multipartRequest) throws Exception;
  public void hoteDetail(HttpServletRequest request, int hotelNo, Model model);
}
