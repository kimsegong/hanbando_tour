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
  private int userNo;
  private int regionNo;
  private int themeNo;
  private String packageTitle;
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

}
