package com.tour.hanbando.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import com.tour.hanbando.dto.ReserveDto;

public interface ReserveService {

  public void loadReserveList(HttpServletRequest request, Model model);
  public void loadReserveListByUser(HttpServletRequest request, Model model);
  public ReserveDto loadReserve(int reserveNo);
  
  public Map<String, Object> loadTourists(HttpServletRequest request);
  
  public int removeReserve(HttpServletRequest request);
}
