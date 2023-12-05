package com.tour.hanbando.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

public interface FaqService {
  public void loadFaqList(HttpServletRequest request, Model model);
}
