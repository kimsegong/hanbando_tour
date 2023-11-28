package com.tour.hanbando.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tour.hanbando.dao.HotelMapper;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class HotelServiceImpl implements HotelService {
  private final HotelMapper hotelMapper;
  
}
