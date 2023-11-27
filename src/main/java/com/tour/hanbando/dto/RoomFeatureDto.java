package com.tour.hanbando.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RoomFeatureDto {
  private int roomNo;
  private int towel; // 0 없음, 1 있음
  private int water; // 0 없음, 1 있음
  private int coffee; // 0 없음, 1 있음
  private int drier; // 0 없음, 1 있음
  private int iron; // 0 없음, 1 있음
  private int minibar; // 0 없음, 1 있음
}
