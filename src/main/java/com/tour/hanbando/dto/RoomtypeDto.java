package com.tour.hanbando.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RoomtypeDto {
  private int roomNo;
  private int hotelNo;
  private String roomDetail;
  private String roomName;
  private int roomMany;
  private String rView;
  private int bleakfast;
  private int smoke;
  private int people;
  private String bed;
  private String shower;
  private int rSize;
}
