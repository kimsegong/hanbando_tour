package com.tour.hanbando.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class InquiryDto {
  private int inquiryNo;
  private int userNo;
  private String title;
  private String contents;
  private String createdAt;
  private String separate;
}

