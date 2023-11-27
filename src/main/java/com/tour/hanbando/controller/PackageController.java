package com.tour.hanbando.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tour.hanbando.service.PackageService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/package")
@RequiredArgsConstructor
@Controller
public class PackageController {

  private final PackageService packageService;
  
  @GetMapping("/list.do")
    public String list() {
     //model.addAttribute("count", packageService.getTotalPackageCount());
     return "package/list"; 
    }

  
  @ResponseBody
  @GetMapping(value="/getList.do", produces="application/json")
  public Map<String, Object> getList(HttpServletRequest request){
    return packageService.getPackageList(request);
  }  


}