package com.tour.hanbando.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.tour.hanbando.dao.FaqMapper;
import com.tour.hanbando.dao.NoticeMapper;
import com.tour.hanbando.dto.FaqDto;
import com.tour.hanbando.dto.NoticeDto;

import lombok.RequiredArgsConstructor;


@Transactional
@RequiredArgsConstructor
@Service
public class FaqServiceImpl implements FaqService {
  private final FaqMapper faqMapper;
  
@Transactional(readOnly=true)  
@Override
public void loadFaqList(HttpServletRequest request, Model model) {
  // TODO Auto-generated method stub
  
  Map<String, Object> map = new HashMap<>();
  
  List<FaqDto> faqList = faqMapper.getFaqList(map);
  
  model.addAttribute("faqList", faqList);
  
}
}

