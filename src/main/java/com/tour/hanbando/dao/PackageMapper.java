package com.tour.hanbando.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.tour.hanbando.dto.PackageDto;
import com.tour.hanbando.dto.ProductImageDto;

@Mapper
public interface PackageMapper {
  public int getPackageCount();
  public List<PackageDto> getPackageList(Map<String, Object> map);
  public int insertPackage(PackageDto packageDto);
  public int insertPackageImage(ProductImageDto image);
  public int insertThumbnail(ProductImageDto image);
}
