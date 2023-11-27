package com.tour.hanbando.controller;

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
  
  @GetMapping("/write.form")
  public String write() {
   return "package/write"; 
  }
  @PostMapping("/add.do")
  public String add(MultipartHttpServletRequest multipartRequest, RedirectAttributes redirectAttributes) throws Exception {
     // int addResult = packageService.addPackage(multipartRequest);

//      if (addResult == 1) {
//          redirectAttributes.addFlashAttribute("successMessage", "Package added successfully!");
//      } else {
//          redirectAttributes.addFlashAttribute("errorMessage", "Failed to add package. Please try again.");
//      }
  
      return "redirect:/package/list.do";
  }

  @GetMapping("/increseHit.do")
  public String increseHit(@RequestParam(value="packageNo", required=false, defaultValue="0") int packageNo) {
    //int increseResult = packageService.increseHit(packageNo);
//    if(increseResult == 1) {
//      return "redirect:/package/detail.do?packageNo=" + packageNo;
//    } else {
      return "redirect:/package/list.do";
  //  }
  }
  
//  @ResponseBody
//  @GetMapping(value="/getList.do", produces="application/json")
//  public Map<String, Object> getList(HttpServletRequest request){
//    return packageService.getPackageList(request);
//  }  

  @GetMapping("/detail.do")
  public String detail(@RequestParam(value="packageNo", required=false, defaultValue="0") int packageNo
          , Model model) {
 //  PackageDto package = packageService.getPackage(packageNo);
  // model.addAttribute("package", package);
   return "package/detail"; 
  }

}