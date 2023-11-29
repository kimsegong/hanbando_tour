package com.tour.hanbando.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.tour.hanbando.dto.NoticeDto;

@Mapper
public interface NoticeMapper {
  public List<NoticeDto> getNoticeList(Map<String, Object> map);
  public int getNoticeCount();
  public List<NoticeDto> getSearchList(Map<String, Object> map);
  public int getSearchCount(Map<String, Object> map);
  public int insertNotice(NoticeDto notice);
  public NoticeDto getNotice(int noticeNo);
}