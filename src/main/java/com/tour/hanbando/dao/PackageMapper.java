package com.tour.hanbando.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.tour.hanbando.dto.HeartDto;
import com.tour.hanbando.dto.PackageDto;
import com.tour.hanbando.dto.ProductImageDto;
import com.tour.hanbando.dto.RegionDto;
import com.tour.hanbando.dto.ReserveDto;
import com.tour.hanbando.dto.ReviewDto;
import com.tour.hanbando.dto.ThemeDto;

@Mapper
public interface PackageMapper {
  
  // 패키지상품 리스트 불러오기
  public int getPackageCount(int regionNo, String condition);
  public int getCount();
  public List<PackageDto> getPackageList(Map<String, Object> map);
  public List<ProductImageDto> getPackageImageList(int packageNo);
  public List<ProductImageDto> getPackageThumbnailList(Map<String, Object> map);
  public ProductImageDto getImage(int imageNo);
  
  // 패키지상품 작성하기
  public int insertPackage(PackageDto packageDto);
  public int insertPackageImage(ProductImageDto image);
  public int insertTumbnailImage(ProductImageDto image);
  public int insertImageList(ProductImageDto image);
  public int insertPackageTh(PackageDto packageDto);
  public int insertRegion(RegionDto region);
  public int insertTheme(ThemeDto theme);
  
  // 패키지상품 상세보기
  public PackageDto getPackage(int packageNo);
  public List<RegionDto> getRegion(Map<String, Object> map);
  public List<ThemeDto> getTheme(Map<String, Object> map);
  
  // 패키지상품 수정
  public int updatePackage(PackageDto packageDto);
  
  // 조회수
  public int packageHit(int packageNo);
  public List<PackageDto> getHitList(Map<String, Object> map);
  
  // 삭제
  public int deletePackageImage(String filesystemName);
  public int deletePackageImageList(int productNo);
  public int deletePackage(int packageNo);
  
  // 리뷰
  public int insertReview(ReviewDto review);
  public int getReviewCount(int productNo);
  public List<ReviewDto> getReviewList(Map<String, Object> map);
  public List<ReviewDto> getReviewStarList(Map<String, Object> map);
  public double starAve(int packageNo);
  public int deleteReview(int reviewNo);
  
  // 부가요소
  public List<ReserveDto> getReserve(int packageNo);
  public PackageDto getPackageInfoByReservationStart(Map<String, Object> map);
  
  // 찜 
  public int heartProduct(HeartDto heart);
  public List<HeartDto> selectHeartList(Map<String, Object> map);
  public int getHeartCount(int userNo);
  public int checkHeart(Map<String, Object> map);
  public int deleteHeart(int packageNo);
}
