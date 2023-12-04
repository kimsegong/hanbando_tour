package com.tour.hanbando.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.tour.hanbando.dto.PackageDto;
import com.tour.hanbando.dto.ReserveDto;

public interface PackageService {
  public Map<String, Object> getPackageList(HttpServletRequest request, String condition, int recommendStatus);
  public int getTotalPackageCount();
  public Map<String, Object> addPackage(MultipartHttpServletRequest multipartRequest) throws Exception;
  public boolean addThumbnail(MultipartHttpServletRequest multipartRequest) throws Exception;
  public Map<String, Object> getAttachList(HttpServletRequest request);
  public void loadUpload(HttpServletRequest request, Model model) ;
  public int addRegion(HttpServletRequest request);
  public int addTheme(HttpServletRequest request);
  public List<String> getEditorImageList(String packageContents);
  public PackageDto getPackage(int packageNo);
  public Map<String, Object> imageUpload(MultipartHttpServletRequest multipartRequest);
  public int modifyPackage(HttpServletRequest request);
  public int increseHit(int packageNo);
  public Map<String, Object> getHit(HttpServletRequest request);
  public void getRegionAndTheme(HttpServletRequest request, Model model);
  public int removePackage(int packageNo);
  public Map<String, Object> addReview(HttpServletRequest request);
  public Map<String, Object> loadReviewList(HttpServletRequest request);
  public Map<String, Object> loadReviewStarList(HttpServletRequest request);
  public int getAverageRating(int packageNo);
  public int addHeart(HttpServletRequest request);
  public Map<String, Object> removeReview(int reviewNo);
  public List<ReserveDto> getReserveUser(int packageNo);
  public void getHeartPackage(HttpServletRequest request, Model model);
  
}
