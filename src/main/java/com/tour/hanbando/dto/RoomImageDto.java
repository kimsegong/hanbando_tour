package com.tour.hanbando.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RoomImageDto {
  private int hotelNo;
  private Integer roomNo; // Nullable
  private Integer thumbnail; // Nullable
  private String filesystemName;
  private String imagePath;
}
