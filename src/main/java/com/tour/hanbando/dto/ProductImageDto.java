package com.tour.hanbando.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductImageDto {
  private int imageNo;
  private int packageNo;
  private int hotelNo;
  private int thumbnail;
  private String filesystemName;
  private String imagePath;
}
