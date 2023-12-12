package com.tour.hanbando.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class NoticeAttachDto {
  private int attachNo;
  private String path;
  private String originalFilename;
  private String filesystemName;
  private NoticeDto noticeDto;
}

