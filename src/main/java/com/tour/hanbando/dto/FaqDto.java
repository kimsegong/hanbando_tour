package com.tour.hanbando.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FaqDto {
  private int faqNo;
  private int caNo;
  private String title;
  private String contents;
  private String createdAt;
  private String modifiedAt;
}
