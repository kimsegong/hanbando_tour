package com.tour.hanbando.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
  
  @ResponseBody
  @GetMapping("searchPackage.do")
  public Map<String, Object> searchPackageList(HttpServletRequest request){
    return mainService.SearchPackageList(request);
  }
  
  @ResponseBody
  @GetMapping("searchHotel.do")
  public Map<String, Object> searchHotelList(HttpServletRequest request){
    return mainService.SearchHotelList(request);
  }
  
  @ResponseBody
  @GetMapping("getBest.do")
  public Map<String, Object> bestPackage(){
    return mainService.getBestPackage();
  }
  @ResponseBody
  @GetMapping("getTheme.do")
  public Map<String, Object> themePackage(){
    return mainService.getThemePackage();
  }
  
  @GetMapping("/bannerList.do")
  public String banner(Model model) {
    mainService.bannerList(model);
    return "manage/bannerList";
  }
  
  @PostMapping("bannerAdd.do")
  public String addBanner(MultipartHttpServletRequest multipartRequest
      , RedirectAttributes redirectAttributes) throws Exception {
    
    redirectAttributes.addFlashAttribute("modifyResult", mainService.addBannerImage(multipartRequest));
    return "redirect:/main/bannerList.do";
  }
  
  
  
}
