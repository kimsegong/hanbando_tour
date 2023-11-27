package com.tour.hanbando.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface PackageService {
  public Map<String, Object> getPackageList(HttpServletRequest request);
  public int addPackage(MultipartHttpServletRequest multipartRequest) throws Exception;
  public List<String> getEditorImageList(String contents);
}
