package com.tour.hanbando.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import com.tour.hanbando.dto.FaqCaDto;
import com.tour.hanbando.dto.FaqDto;

public interface FaqService {
  public Map<String, Object> loadFaqList(HttpServletRequest request);
<<<<<<< HEAD
  public FaqCaDto loadFaqCaList(HttpServletRequest request, Model model);
=======
  public Map<String, Object>loadFaqCaList(HttpServletRequest request, Model model);
>>>>>>> 66c6e8c6c300ed2a05fad51ec6fdbd6453531d82
  public void loadFaqCashList(HttpServletRequest request, Model model);
  public void loadFaqKoreaList(HttpServletRequest request, Model model);
  public void loadFaqMemberList(HttpServletRequest request, Model model);
  public Map<String, Object> loadOfList(HttpServletRequest request);
  public int addFaq(HttpServletRequest request);
  public void getFaqDetail(HttpServletRequest request, Model model);
  public int addFaqDetail(HttpServletRequest request);
  public Map<String, Object> removeFaq(int faqNo);
  public int modifyFaq(HttpServletRequest request);
  public FaqDto getFaq(int faqNo);
  public FaqCaDto loadFaqCa(int faqCaNo);
}