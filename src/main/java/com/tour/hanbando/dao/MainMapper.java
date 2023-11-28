package com.tour.hanbando.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.tour.hanbando.dto.PackageDto;

@Mapper
public interface MainMapper {
  public List<PackageDto> searchPackageList(Map<String, Object> map);
  public int countSearchPackage(Map<String, Object> map);
}
