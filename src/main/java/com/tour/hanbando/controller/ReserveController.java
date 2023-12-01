package com.tour.hanbando.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tour.hanbando.dto.PackageDto;
import com.tour.hanbando.dto.ReserveDto;
import com.tour.hanbando.dto.UserDto;
import com.tour.hanbando.service.PackageService;
import com.tour.hanbando.service.ReserveService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/reserve")
@RequiredArgsConstructor
@Controller
public class ReserveController {

  private final ReserveService reserveService;
  private final PackageService packageService;
  
  @GetMapping("/reserveList.do")
  public String list(HttpServletRequest request, Model model) {
    if(request.getParameter("userNo") != null) { // userNo가 파라미터로 넘어오는 경우 
      reserveService.loadReserveListByUser(request, model);
    } else { // 파라미터가 없는 경우
      reserveService.loadReserveList(request, model);
    }
    return "reserve/list";
  }
  
  
  @GetMapping("/write.form")
  public String reserve(HttpServletRequest request, Model model) {
    PackageDto pack = packageService.getPackage(Integer.parseInt(request.getParameter("packageNo")));
    model.addAttribute("pack", pack);
//    model.addAttribute("resDate", request.getParameter("resDate"));
    return "reserve/write";
  }
  
  @GetMapping("/detail.do")
  public String detail(@RequestParam(value="reserveNo", required = false, defaultValue = "0") int reserveNo, Model model) {
    ReserveDto reserve = reserveService.loadReserve(reserveNo);
    model.addAttribute("reserve", reserve);
    return "reserve/detail";
  }
  
  @PostMapping("/edit.form")
  public String edit(@ModelAttribute("reserve") ReserveDto reserve, Model model, HttpServletRequest request) {
    String userNo = request.getParameter("userNo");
    String userName = request.getParameter("userName");
    String userMobile = request.getParameter("userMobile");
    model.addAttribute("userNo", userNo);
    model.addAttribute("userName", userName);
    model.addAttribute("userMobile", userMobile);
    return "reserve/edit";
  }
 
  
  @ResponseBody
  @GetMapping(value="/getTouristInfo.do", produces="application/json")
  public Map<String, Object> getTourists(HttpServletRequest request) {
    return reserveService.loadTourists(request);
  }
  
  
  @ResponseBody
  @PostMapping(value="/addReserve.do", produces="application/json")
  public Map<String, Object> addReserve(HttpServletRequest request, RedirectAttributes redirectAttributes) throws Exception {
    return reserveService.addReserve(request);
  }
  
  @PostMapping("/addTourist.do")
  public String addTourist(HttpServletRequest request, RedirectAttributes redirectAttributes) throws Exception {
    int addTouristResult = reserveService.addTourist(request);
    redirectAttributes.addFlashAttribute("addTouristResult", addTouristResult);
    return "redirect:/reserve/detail.do?reserveNo=" + request.getParameter("reserveNo");
  }

  
  @PostMapping("/modifyReserve.do")
  public String modifyBlog(HttpServletRequest request, RedirectAttributes redirectAttributes) {
    int modifyResult = reserveService.modifyReserve(request);
    redirectAttributes.addFlashAttribute("modifyResult", modifyResult);
    return "redirect:/reserve/detail.do?reserveNo=" + request.getParameter("reserveNo");
  }
  
  
  @PostMapping("/delete.do")
  public String removeReserve(HttpServletRequest request, RedirectAttributes redirectAttributes) {
    redirectAttributes.addFlashAttribute("removeResult", reserveService.removeReserve(request));
    return "redirect:/reserve/reserveList.do?userNo=" + request.getParameter("userNo");
  }
  
  
  
}
