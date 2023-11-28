package com.tour.hanbando.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tour.hanbando.service.ReserveService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/reserve")
@RequiredArgsConstructor
@Controller
public class ReserveController {

  private final ReserveService reserveService;
  
  @GetMapping("/reserveList.do")
  public String list(HttpServletRequest request, Model model) {
    reserveService.loadReserveList(request, model);
    return "reserve/list";
  }
  
  @RequestMapping("/write.form")
  public String write() {
    return "reserve/write";
  }
  
  @RequestMapping("/detail.do")
  public String detail(HttpServletRequest request, Model model) {
    return "reserve/detail";
  }
  
  @RequestMapping("/edit.form")
  public String edit() {
    return "reserve/edit";
  }
  
}
