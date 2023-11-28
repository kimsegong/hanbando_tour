package com.tour.hanbando.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tour.hanbando.dto.PackageDto;
import com.tour.hanbando.service.PackageService;


import lombok.RequiredArgsConstructor;

@RequestMapping("/package")
@RequiredArgsConstructor
@Controller
public class PackageController {

  private final PackageService packageService;
  
  @GetMapping("/list.do")
    public String list() {
     //model.addAttribute("count", packageService.getTotalpackageCount());
     return "package/list"; 
    }
  
  @ResponseBody
  @GetMapping(value="/getList.do", produces="application/json")
  public Map<String, Object> getList(HttpServletRequest request){
    return packageService.getPackageList(request);
  }  

  @GetMapping("/write.form")
  public String write(Model model) {
	 Map<String, Object> result = packageService.getRegionAndTheme(new HashMap<>());	    
	 model.addAttribute("regionList", result.get("regionList"));
	 model.addAttribute("themeList", result.get("themeList"));
	 return "package/write"; 
	}
  
  @GetMapping("/regionWrite.form")
  public String regionWrite() {
    return "package/regionWrite"; 
  }
  
  @GetMapping("/themeWrite.form")
  public String detailWrite() {
    return "package/themeWrite"; 
  }
  
  @PostMapping("/add.do")
  public String add(MultipartHttpServletRequest multipartRequest, RedirectAttributes redirectAttributes) throws Exception {
      int addResult = packageService.addPackage(multipartRequest);

      if (addResult == 1) {
          redirectAttributes.addFlashAttribute("successMessage", "package added successfully!");
      } else {
          redirectAttributes.addFlashAttribute("errorMessage", "Failed to add package. Please try again.");
      }
      return "redirect:/package/list.do";
  }
  
  @PostMapping("/addRegion.do")
  public String addRegion(HttpServletRequest request) {
      packageService.addRegion(request);
      return "redirect:/package/write.form";
  }
  @PostMapping("/addTheme.do")
  public String addTheme(HttpServletRequest request) {
    packageService.addTheme(request);
    return "redirect:/package/write.form";
  }
  
  @GetMapping("/detail.do")
  public String detail(@RequestParam(value="packageNo", required=false, defaultValue="0") int packageNo
          , Model model) {
    	PackageDto packageDto = packageService.getPackage(packageNo);
    	model.addAttribute("packageDto", packageDto);
    	return "package/detail"; 
	}
  
  @GetMapping("/increseHit.do")
  public String increseHit(@RequestParam(value="packageNo", required=false, defaultValue="0") int packageNo) {
    int increseResult = packageService.increseHit(packageNo);
    if(increseResult == 1) {
      return "redirect:/package/detail.do?packageNo=" + packageNo;
    } else {
      return "redirect:/package/list.do";
    }
  }
  
  @ResponseBody
  @GetMapping(value="/hitList.do", produces="application/json")
  public Map<String, Object> hitList(HttpServletRequest request){
    return packageService.getHit(request);
  }
  


}