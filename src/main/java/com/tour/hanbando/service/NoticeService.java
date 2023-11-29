package com.tour.hanbando.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import com.tour.hanbando.dto.NoticeDto;

public interface NoticeService {
  public void loadNoticeList(HttpServletRequest request, Model model);
  public int  addNotice(HttpServletRequest request);
  public void LoadSearchList(HttpServletRequest request, Model model);
  public NoticeDto loadNotice(int noticeNo);
}
