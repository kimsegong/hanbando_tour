package com.tour.hanbando.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ReserveDto {

  private int reserveNo;
  private String reservedAt;
  private String requestedTerm;
  private int agree;
  private String departureLoc;
  private int reserveStatus;
  private String reserveStart;
  private String reserveFinish;
  private int reservePerson;
  private int reservePrice;
//  private UserDto userDto;
  private PackageDto packageDto;
//  private HotelDto hotelDto;
//  private RoomtypeDto roomtypeDto;
  
}
