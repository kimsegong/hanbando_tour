package com.tour.hanbando.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.tour.hanbando.dto.BannerImageDto;
import com.tour.hanbando.dto.HotelDto;
import com.tour.hanbando.dto.PackageDto;

@Mapper
public interface MainMapper {
  public List<PackageDto> searchPackageList(Map<String, Object> map);
  public int countSearchPackage(Map<String, Object> map);
  public List<HotelDto> searchHotelList(Map<String, Object> map);
  public int countSearchHotel(Map<String, Object> map);
  public List<PackageDto> getBestPackage();
  public int countTheme();
  public List<PackageDto> getThemePackage(int themeNo);
  public List<BannerImageDto> getBannerImage();
  public BannerImageDto getNoBannerImage(int bannerNo);
  public int updateBannerImage(BannerImageDto bannerImageDto);
  public int updateAddress(BannerImageDto bannerImageDto);
}
