package com.tour.hanbando.service;

import org.springframework.stereotype.Service;

import com.tour.hanbando.dao.ReserveMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ReserveServiceImpl implements ReserveService {

  private final ReserveMapper reserveMapper;
  
}
