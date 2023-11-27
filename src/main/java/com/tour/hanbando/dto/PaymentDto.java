package com.tour.hanbando.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PaymentDto {

  private int paymentNo;
  private String impUid;
  private String payYn;
  private String payMethod;
  private int paidAmount;
  private String paidAt;
  private String merchantUid;
  private String buyerName;
  private String buyerEmail;
  private String errorMsg;
  private String payStatus;
  private ReserveDto reserveDto;
  
}
