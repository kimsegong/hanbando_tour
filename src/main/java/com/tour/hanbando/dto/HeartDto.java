package com.tour.hanbando.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HeartDto {
  private int userNo;
  private PackageDto packageDto;
  private HotelDto hotelDto;
}
