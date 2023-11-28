package com.tour.hanbando.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.tour.hanbando.dto.PackageDto;

public interface PackageService {
  public Map<String, Object> getPackageList(HttpServletRequest request);
  public int addPackage(MultipartHttpServletRequest multipartRequest) throws Exception;
  public int addRegion(HttpServletRequest request);
  public int addTheme(HttpServletRequest request);
  public List<String> getEditorImageList(String contents);
  public PackageDto getPackage(int packageNo);
  public int increseHit(int packageNo);
  public Map<String, Object> getHit(HttpServletRequest request);
  public Map<String, Object> getRegionList(HttpServletRequest request);
  public Map<String, Object> getThemeList(HttpServletRequest request);
  
}
