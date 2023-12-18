package com.tour.hanbando.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class HotelFileUtils {

  // 블로그 작성시 사용된 이미지가 저장될 경로 반환하기
  public String getBlogImagePath() {
    LocalDate today = LocalDate.now();
    return "/hanbando/hotel";
  }
  
  //블로그 이미지가 저장된 어제 경로를 반환
  public String getBlogImagePathInYesterday() {
    LocalDate date = LocalDate.now();
    date = date.minusDays(1); // 1일 전
    return "/hanbando/hotel" ;
  }
  
  // 업로드 게시판 작성시 첨부한 파일이 저장될 경로 반환하기
  public String getUploadPath() {
    LocalDate today = LocalDate.now();
    return "/hanbando/hotel" ;
  }
  
  // 임시 파일이 저장될 경로 반환하기(zip파일)
  public String getTempPath() {
    return "/hanbando/temp";
  }
  
  
  // 파일이 저장될 이름 반환하기
  public String getFilesystemName(String originalFileName) {
    
    /*  UUID.확장자  */
    
    String extName = null;
    if(originalFileName.endsWith("tar.gz")) {  // 확장자에 마침표가 포함되는 예외 경우를 처리한다.
      extName = "tar.gz";
    } else {
      String[] arr = originalFileName.split("\\.");  // 정규식에서 "." 는 "모든문자"이므로 [.] 또는 \\. 사용
      extName = arr[arr.length - 1];
    }
    return UUID.randomUUID().toString().replace("-", "") + "." + extName;
  }
  
  
  // 임시 파일 이름 반환하기(확장자는 제외하고 이름만 반환)
  public String getTempFilename() {
    return System.currentTimeMillis() + "";
  }
  
  
}
