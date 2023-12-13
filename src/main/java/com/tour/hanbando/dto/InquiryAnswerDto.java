package com.tour.hanbando.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class InquiryAnswerDto {

  private int answerNo;
  private int userNo;
  private int inquiryNo;
  private String contents;
  private String createdAt;
}
