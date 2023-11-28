package com.tour.hanbando.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tour.hanbando.service.MainService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/main")
@RequiredArgsConstructor
@Controller
public class MainController {
  private final MainService mainService;
 
  @GetMapping("search.do")
  public String mainSearch(String query, Model model) {
    model.addAttribute("query", query);
    return "main/search";
  }
  
  @GetMapping("searchlist.do")
  public Map<String, Object> SearchList(HttpServletRequest request){
    return mainService.SearchPackageList(request);
  }
  
  
}
