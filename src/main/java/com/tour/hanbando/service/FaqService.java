package com.tour.hanbando.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

public interface FaqService {
  public void loadFaqList(HttpServletRequest request, Model model);
  public void loadFaqCashList(HttpServletRequest request, Model model);
  public void loadFaqKoreaList(HttpServletRequest request, Model model);
  public void loadFaqMemberList(HttpServletRequest request, Model model);
  public Map<String, Object> loadOfList(HttpServletRequest request);

}
