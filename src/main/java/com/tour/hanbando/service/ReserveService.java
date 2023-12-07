package com.tour.hanbando.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tour.hanbando.dto.HotelDto;
import com.tour.hanbando.dto.PaymentDto;
import com.tour.hanbando.dto.ReserveDto;

public interface ReserveService {

  public Map<String, Object> addReserve(HttpServletRequest request) throws Exception;
  public int addTourist(HttpServletRequest request) throws Exception;
  public Map<String, Object> addPayment(HttpServletRequest request, PaymentDto payment);
  
  public void loadReserveList(HttpServletRequest request, Model model);
  public void loadReserveListByUser(HttpServletRequest request, Model model);
  public void loadReserveHotelListByUser(HttpServletRequest request, Model model);
  public HotelDto loadHotelInfoWithWriteform(int hotelNo);
  
  public ReserveDto loadReserve(int reserveNo);
  public Map<String, Object> loadTourists(HttpServletRequest request);
  public ReserveDto loadReserveHotel(int reserveNo);
  
  public PaymentDto loadPaymentByReserveNo(int reserveNo);
  public Map<String, Object> loadPaymentByMerchantUid(HttpServletRequest request, PaymentDto payment);
  
  public int modifyReserve(HttpServletRequest request);
  public int removeReserve(HttpServletRequest request);
  
  public Map<String, Object> modifyReserveStatusByPayStatus(Map<String, String> payload, HttpServletRequest request, RedirectAttributes redirectAttributes);
  
  
  // 아임포트 accessToken 발급
  public String getAccessToken(String apiKey, String apiSecret);
  
}
