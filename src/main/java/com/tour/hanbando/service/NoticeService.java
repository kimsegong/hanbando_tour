package com.tour.hanbando.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.tour.hanbando.dto.NoticeDto;

public interface NoticeService {
  public void loadNoticeList(HttpServletRequest request, Model model);
  public int  addNotice(HttpServletRequest request);
  public void LoadSearchList(HttpServletRequest request, Model model);
  public NoticeDto loadNotice(int noticeNo);
  public int modifyNotice(HttpServletRequest request);
  public int removeNotice(int NoticeNo);
  public Map<String, Object> imageUpload(MultipartHttpServletRequest multipartRequest);
}
