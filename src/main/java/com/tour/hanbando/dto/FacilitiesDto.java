package com.tour.hanbando.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FacilitiesDto {
  private int hotelNo;
  private int pool;
  private int morning;
  private int sauna;
  private int lounge;
  private int roomservice;
}
