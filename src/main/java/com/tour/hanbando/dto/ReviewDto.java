package com.tour.hanbando.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewDto {
  private int reviewNo;
  private int userNo;
  private int reserveNo;
  private int packageNo;
  private int hotelNo;
  private String reviewContents;
  private int star;
  private String registAt;
}
