package com.tour.hanbando.controller;

import java.util.HashMap;
import java.util.List;
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
import com.tour.hanbando.dto.ReserveDto;
import com.tour.hanbando.service.PackageService;


import lombok.RequiredArgsConstructor;

@RequestMapping("/package")
@RequiredArgsConstructor
@Controller
public class PackageController {

  private final PackageService packageService;
  
  @GetMapping("/list.do")
    public String list(Model model) {     
     return "package/list"; 
    }
  
  @ResponseBody
  @GetMapping(value="/getList.do", produces="application/json")
  public Map<String, Object> getList(@RequestParam(value = "condition", required = false, defaultValue = "defaultCondition") String condition, 
		  @RequestParam(value = "recommendStatus", required = false, defaultValue = "0") int recommendStatus , 
		  HttpServletRequest request){	
	  return packageService.getPackageList(request, condition, recommendStatus);
  }  
  
  @GetMapping("/write.form")
  public String write(HttpServletRequest request, Model model) {
	 packageService.getRegionAndTheme(request, model);
	 return "package/write"; 
	}
  
  @PostMapping("/edit.do")
  public String edit(@RequestParam(value="packageNo", required=false, defaultValue="0") int packageNo, 
      HttpServletRequest request, Model model) {
    PackageDto packageDto = packageService.getPackage(packageNo);
    packageService.getRegionAndTheme(request, model);    
    model.addAttribute("packageDto", packageDto);
    return "package/edit";
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
      redirectAttributes.addFlashAttribute("map", packageService.addPackage(multipartRequest));
      return "redirect:/package/thumbnail.do";
  }

  
  @GetMapping("/thumbnail.do")
  public String thumbnailWrite() {
	  return "package/thumbnail"; 
  }
  
  @PostMapping("/addThumbnail.do")
  public String addThumbnail(MultipartHttpServletRequest multipartRequest, RedirectAttributes redirectAttributes) throws Exception {
	  boolean addResult = packageService.addThumbnail(multipartRequest);
	  redirectAttributes.addFlashAttribute("addResult", addResult);
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
  
  @ResponseBody
  @PostMapping(value="/imageUpload.do", produces="application/json")
  public Map<String, Object> imageUpload(MultipartHttpServletRequest multipartRequest) {
    return packageService.imageUpload(multipartRequest);
  }
  
  @GetMapping("/detail.do")
  public String detail(
          @RequestParam(value = "packageNo", required = false, defaultValue = "0") int packageNo, 
          HttpServletRequest request, Model model) {
      List<ReserveDto> reserve = packageService.getReserveUser(packageNo);
      PackageDto packageDto = packageService.getPackage(packageNo);
      model.addAttribute("reserve", reserve);
      model.addAttribute("packageDto", packageDto);
      model.addAttribute("attachList", packageService.getAttachList(request));  
      return "package/detail";
  }

  
  @PostMapping("/modifyPackage.do")
  public String modifyProduct(HttpServletRequest request, RedirectAttributes redirectAttributes) {
    int modifyResult = packageService.modifyPackage(request);
    redirectAttributes.addFlashAttribute("modifyResult", modifyResult);
    return "redirect:/package/detail.do?packageNo=" + request.getParameter("packageNo"); 
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
  
  @PostMapping("/remove.do")
  public String remove(@RequestParam(value="packageNo", required=false, defaultValue="0") int packageNo
                     , RedirectAttributes redirectAttributes) {
    int removeResult = packageService.removePackage(packageNo);
    redirectAttributes.addFlashAttribute("removeResult", removeResult);
    return "redirect:/package/list.do";
  }

  @ResponseBody
  @PostMapping(value="/addReview.do", produces="application/json")
  public Map<String, Object> addReview(HttpServletRequest request) {
    return packageService.addReview(request);
  }
  @ResponseBody
  @PostMapping(value="/getAverageRating.do", produces="application/json")
  public Map<String, Object> starAverage(@RequestParam(value="packageNo", required=false, defaultValue="0") int packageNo) {
      Map<String, Object> response = new HashMap<>();

      try {
          int averageRating = packageService.getAverageRating(packageNo);
          response.put("success", true);
          response.put("averageRating", averageRating);
      } catch (Exception e) {
          response.put("success", false);
          response.put("error", "Failed to get average rating.");
          e.printStackTrace();
      }
      return response;
  }

  @ResponseBody
  @GetMapping(value="/reviewList.do", produces="application/json")
  public Map<String, Object> reviewList(HttpServletRequest request){
    return packageService.loadReviewList(request);
  }
  
  @ResponseBody
  @GetMapping(value="/reviewStarList.do", produces="application/json")
  public Map<String, Object> reviewStarList(HttpServletRequest request){
    return packageService.loadReviewStarList(request);
  }
  
  @ResponseBody
  @PostMapping(value="/removeReview.do", produces="application/json")
  public Map<String, Object> removeReview(@RequestParam(value="reviewNo", required=false, defaultValue="0") int reviewNo) {
    return packageService.removeReview(reviewNo);
  }
  
  @PostMapping(value="/heartPackage.do" , produces="application/json")
  public String heart(HttpServletRequest request, RedirectAttributes redirectAttributes) {
    int heartResult = packageService.addHeart(request);
    redirectAttributes.addFlashAttribute("heartResult", heartResult);
    return "redirect:/package/detail.do?packageNo=" + request.getParameter("packageNo"); 
  }
  
  @ResponseBody
  @GetMapping(value="/checkHeart.do", produces="application/json")
  public Map<String, Object> checkHeart(
          @RequestParam(value="packageNo", required=false, defaultValue="0") int packageNo,
          @RequestParam(value="userNo", required=false, defaultValue="0") int userNo) {
      return packageService.checkHeart(packageNo, userNo);
  }

}