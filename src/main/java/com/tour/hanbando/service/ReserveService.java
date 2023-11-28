package com.tour.hanbando.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

public interface ReserveService {

  public void loadReserveList(HttpServletRequest request, Model model);
  
}
