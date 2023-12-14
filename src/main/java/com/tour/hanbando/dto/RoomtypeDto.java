package com.tour.hanbando.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RoomtypeDto {
  private int hotelNo;
  private int roomNo;
  private String roomDetail;
  private String roomName;
  private int roomMany;
  @JsonProperty("rView")
  private String rView;
  private int bleakfast;
  private int smoke;
  private int people;
  private String bed;
  private String shower;
  @JsonProperty("rSize")
  private int rSize;
}
