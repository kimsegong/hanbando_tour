package com.tour.hanbando.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PackageDto {
  private int packageNo;
  private String packageTitle;
  private String miniOne;
  private String miniTwo;
  private String miniThree;
  private String packagePlan;
  private String packageContents;
  private String hotelContents;
  private int price;
  private String danger;
  private String createdAt;
  private String modifiedAt;
  private int hit;
  private int status;
  private int maxPeople;
  private int recommendStatus;
  private RegionDto regionDto;
  private ThemeDto themeDto;
  private UserDto userDto;

}
