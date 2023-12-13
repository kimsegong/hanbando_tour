package com.tour.hanbando.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.tour.hanbando.dto.NoticeDto;

public interface NoticeService {
  public void loadNoticeList(HttpServletRequest request, Model model);
  public void LoadSearchList(HttpServletRequest request, Model model);
  public void loadNotice(HttpServletRequest request, Model model);
  public NoticeDto getNotice(int noticeNo);
  public int modifyNotice(NoticeDto notice);
  public int removeNotice(int NoticeNo);
  public boolean addNotice(MultipartHttpServletRequest multipartRequest) throws Exception;

  public Map<String, Object> getAttachList(HttpServletRequest request);
  public Map<String, Object> addAttach(MultipartHttpServletRequest multipartRequest) throws Exception;
  public Map<String, Object> removeAttach(HttpServletRequest request);
  
  public ResponseEntity<Resource> download(HttpServletRequest request);
  public ResponseEntity<Resource> downloadAll(HttpServletRequest request);
  public void removeTempFiles();

}
