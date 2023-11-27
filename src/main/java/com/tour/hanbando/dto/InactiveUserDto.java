package com.tour.hanbando.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class InactiveUserDto {
  private int userNo;
  private String email;
  private String pw;
  private String name;
  private String gender;
  private String mobile;
  private String postcode;
  private String roadAddress;
  private String jibunAddress;
  private String detailAddress;
  private int agree;
  private int state;
  private Date pwModifiedAt;
  private Date joinedAt;
  private Date inactivedAt;
}

