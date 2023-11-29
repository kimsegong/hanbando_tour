package com.tour.hanbando.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TouristDto {

  private int touristNo;
  private String name;
  private String birthDate;
  private String gender;
  private String mobile;
  private int ageCase;
  
  @JsonBackReference
  private ReserveDto reserveDto;
  
}
