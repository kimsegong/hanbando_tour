package com.tour.hanbando.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BannerImageDto {
  private int bannerNo;
  private String originalName;
  private String filesystemName;
  private String bannerPath;
  private int state;
  private String linkedAddress;
}
