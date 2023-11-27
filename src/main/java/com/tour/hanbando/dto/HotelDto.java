package com.tour.hanbando.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class HotelDto {
  private int hotelNo;
  private int regionNo;
  private String hotelName;
  private String hotelAddress;
  private double latitude;
  private double longitude;
  private String hotelDetail;
  private String phoneNumber;
  private String HEmail;
  private String createdAt;
  private String modifiedAt;
  private int hit;
  private int status;
  private int recommendStatus;
}
