package com.tour.hanbando.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RoompriceDto {
  private int roomNo;
  private int biPrice; // 0 또는 양수
  private String bsDate; // Nullable
  private String beDate; // Nullable
  private int junPrice; // 0 또는 양수
  private String jsDate; // Nullable
  private String jeDate; // Nullable
  private int sungPrice; // 0 또는 양수
  private String ssDate; // Nullable
  private String seDate; // Nullable
}
