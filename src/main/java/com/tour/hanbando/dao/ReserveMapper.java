package com.tour.hanbando.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.tour.hanbando.dto.PaymentDto;
import com.tour.hanbando.dto.ReserveDto;
import com.tour.hanbando.dto.TouristDto;

@Mapper
public interface ReserveMapper {
  
  public int insertReserve(ReserveDto reserve);
  public int insertTourist(TouristDto tourist);
  public int insertPayment(PaymentDto payment);
  
  public int getReserveCount();
  public int getReserveCountByUserNo(int userNo);
  
  public List<ReserveDto> getReserveList(Map<String, Object> map);
  public List<ReserveDto> getReserveListByUser(Map<String, Object> map);
  public ReserveDto getReserve(int reserveNo);
  
  public List<TouristDto> getTourists(int reserveNo);
  public PaymentDto getPaymentBy(Map<String, Object> map);
  
  public int updateReserve(ReserveDto reserve);
  public int deleteReserve(int reserveNo);
  
  public int updateReserveStatus(Map<String, Object> map);
}
