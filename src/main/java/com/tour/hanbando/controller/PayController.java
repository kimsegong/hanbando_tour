package com.tour.hanbando.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tour.hanbando.dto.PaymentDto;
import com.tour.hanbando.service.ReserveService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/payment")
@Controller
public class PayController {

  private final ReserveService reserveService;
  
  @ResponseBody
  @PostMapping(value="/payReserve.do", produces="application/json")
  public Map<String, Object> payRes(HttpServletRequest request, @RequestBody PaymentDto payment){
    return reserveService.addPayment(request, payment);
  }
  
  @ResponseBody
  @PostMapping("/modifyResStatus")
  public Map<String, Object> modifyReserveStatus(@RequestBody Map<String, String> payload, HttpServletRequest request, RedirectAttributes redirectAttributes) {
    return reserveService.modifyReserveStatusByPayStatus(payload, request, redirectAttributes); 
  }
  
  
}
