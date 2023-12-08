package com.tour.hanbando.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.tour.hanbando.dto.NoticeDto;
import com.tour.hanbando.dto.ProductImageDto;

@Mapper
public interface NoticeMapper {
  public List<NoticeDto> getNoticeList(Map<String, Object> map);
  public int getNoticeCount();
  public List<NoticeDto> getSearchList(Map<String, Object> map);
  public int getSearchCount(Map<String, Object> map);
  public int insertNotice(NoticeDto notice);
  public NoticeDto getNotice(int noticeNo);
  public int deleteNotice(int NoticeNo);
  public int updateNotice(NoticeDto notice);
  public int insertNotices(NoticeDto noticeDto);
  public int insertImageList(ProductImageDto image);
  
  
}