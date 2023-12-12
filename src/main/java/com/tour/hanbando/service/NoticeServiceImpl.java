package com.tour.hanbando.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.tour.hanbando.dao.NoticeMapper;
import com.tour.hanbando.dto.NoticeAttachDto;
import com.tour.hanbando.dto.NoticeDto;
import com.tour.hanbando.util.MyFileUtils;
import com.tour.hanbando.util.MyNoticeUtils;
import com.tour.hanbando.util.MyPageUtils;

import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class NoticeServiceImpl implements NoticeService {
  private final NoticeMapper noticeMapper;
  private final MyPageUtils myPageUtils;
  private final MyNoticeUtils myNoticeUtils;
  private final MyFileUtils myFileUtils;
  
  @Transactional(readOnly=true)
  @Override
  public void loadNoticeList(HttpServletRequest request, Model model) {
   
    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    int page = Integer.parseInt(opt.orElse("1"));
    int total = noticeMapper.getNoticeCount();
    int display = 10;
    
    myPageUtils.setPaging(page, total, display);
    
    Map<String, Object> map = Map.of("begin", myPageUtils.getBegin()
                                   , "end", myPageUtils.getEnd());
    
    List<NoticeDto> noticeList = noticeMapper.getNoticeList(map);
    
    model.addAttribute("noticeList", noticeList);
    model.addAttribute("paging", myPageUtils.getMvcPaging(request.getContextPath() + "/notice/list.do"));
    model.addAttribute("beginNo", total - (page - 1) * display);  
    }
  
  
  

  
  @Transactional(readOnly=true)
  @Override
  public void LoadSearchList(HttpServletRequest request, Model model) {
     
    String column = request.getParameter("column");
    String query = request.getParameter("query");
    
    Map<String, Object> map = new HashMap<>();
    map.put("column", column);
    map.put("query", query);
    
    int total = noticeMapper.getSearchCount(map);
    
    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    String strPage = opt.orElse("1");
    int page = Integer.parseInt(strPage);
    
    int display = 10;
    
    myPageUtils.setPaging(page, total, display);
    
    map.put("begin", myPageUtils.getBegin());
    map.put("end", myPageUtils.getEnd());
    
    List<NoticeDto> NoticeList = noticeMapper.getSearchList(map);
    
    model.addAttribute("noticeList", NoticeList);
    model.addAttribute("paging", myPageUtils.getMvcPaging(request.getContextPath() + "/notice/search.do", "column=" + column + "&query=" + query));
    model.addAttribute("beginNo", total - (page - 1) * display);
  }
  
  @Override
  public void loadNotice(HttpServletRequest request, Model model) {
    Optional<String> opt = Optional.ofNullable(request.getParameter("noticeNo"));
    int noticeNo = Integer.parseInt(opt.orElse("0"));
    
    model.addAttribute("notice", noticeMapper.getNotice(noticeNo));
    model.addAttribute("attachList", noticeMapper.getNoticeAttachList(noticeNo));
  }
  @Transactional(readOnly = true)
  @Override
  public NoticeDto getNotice(int noticeNo) {
    return noticeMapper.getNotice(noticeNo);
  }
  
  @Override
  public int modifyNotice(NoticeDto notice) {
    return noticeMapper.updateNotice(notice);
  }
  
  @Override
  public int removeNotice(int NoticeNo) {
    
    return noticeMapper.deleteNotice(NoticeNo);
  }
  
  @Override
  public boolean addNotice(MultipartHttpServletRequest multipartRequest) throws Exception {
      String title = multipartRequest.getParameter("title");
      String contents = multipartRequest.getParameter("contents");
      
      /* noticeDto생성 */
      NoticeDto notice = NoticeDto.builder()
                           .title(title)
                           .contents(contents)
                           .build();
      
      int noticeCount = noticeMapper.insertNotice(notice);

      // 파일 업로드 및 이미지 처리
      List<MultipartFile> files = multipartRequest.getFiles("files");
      
      // 파일 개수로는 첨부파일의 유무를 알 수 없음
      // 파일 이름과 파일 사이즈로 구분
      int attachCount;
      if(files.get(0).getSize() == 0) {
        attachCount = 1;
      } else {
        attachCount = 0;
      }              

      for (MultipartFile multipartFile : files) {
        if (multipartFile != null && !multipartFile.isEmpty()) {
          String path = myNoticeUtils.getUploadPath();
          File dir = new File(path);
          if (!dir.exists()) {
              dir.mkdirs();
          }
          
          String originalFilename = multipartFile.getOriginalFilename(); //원래파일명
          String filesystemName = myNoticeUtils.getFilesystemName(originalFilename); //저장할파일명
          File file = new File(dir, filesystemName);

          multipartFile.transferTo(file); // 실제로 파일이 저장되는 단계

          NoticeAttachDto attach = NoticeAttachDto.builder()
              .path(path)
              .originalFilename(originalFilename)
              .filesystemName(filesystemName)
              .noticeDto(NoticeDto.builder()
                           .noticeNo(notice.getNoticeNo())
                           .build())
              .build();

          // 이미지 추가 결과 저장
          attachCount += noticeMapper.insertNoticeAttach(attach);
        }
      }         

    // 성공 시 true, 실패 시 false 반환
    return (noticeCount == 1) && (files.size() == attachCount);
  }
  
  

      

  
  // 여기서부터 막만진거다
  @Override
  public Map<String, Object> addAttach(MultipartHttpServletRequest multipartRequest) throws Exception {
    List<MultipartFile> files =  multipartRequest.getFiles("files");
    
    int attachCount;
    if(files.get(0).getSize() == 0) {
      attachCount = 1;
    } else {
      attachCount = 0;
    }
    
    for(MultipartFile multipartFile : files) {
      
      if(multipartFile != null && !multipartFile.isEmpty()) {
        
        String path = myFileUtils.getUploadPath();
        File dir = new File(path);
        if(!dir.exists()) {
          dir.mkdirs();
        }
        
        String originalFilename = multipartFile.getOriginalFilename();
        String filesystemName = myFileUtils.getFilesystemName(originalFilename);
        File file = new File(dir, filesystemName);
        
        multipartFile.transferTo(file);
        
        
        NoticeAttachDto attach = NoticeAttachDto.builder()
                            .path(path)
                            .originalFilename(originalFilename)
                            .filesystemName(filesystemName)
                            .noticeDto(NoticeDto.builder()
                                         .noticeNo(Integer.parseInt(multipartRequest.getParameter("noticeNo")))
                                         .build())
                            .build();
        
        attachCount += noticeMapper.insertNoticeAttach(attach);
        
      }  // if
      
    }  // for
    
    return Map.of("attachResult", files.size() == attachCount);
      }
  
  @Override
  public Map<String, Object> getAttachList(HttpServletRequest request) {
    Optional<String> opt = Optional.ofNullable(request.getParameter("noticeNo"));
    int noticeNo=Integer.parseInt(opt.orElse("0"));
    return Map.of("attachList", noticeMapper.getNoticeAttachList(noticeNo));
  }
  
  @Override
  public Map<String, Object> removeAttach(HttpServletRequest request) {
    Optional<String> opt = Optional.ofNullable(request.getParameter("attachNo"));
    int attachNo = Integer.parseInt(opt.orElse("0"));
    
    // 파일 삭제
    NoticeAttachDto attach = noticeMapper.getNoticeAttach(attachNo);
    File file = new File(attach.getPath(), attach.getFilesystemName());
    if(file.exists()) {
      file.delete();
    }
    
    int removeAttachResult = noticeMapper.deleteAttach(attachNo);
    return Map.of("removeAttachResult" , removeAttachResult);
  }
  
  @Override
  public ResponseEntity<Resource> download(HttpServletRequest request) {
    
    // 첨부 파일의 정보 가져오기
    int attachNo = Integer.parseInt(request.getParameter("attachNo"));
    NoticeAttachDto attach = noticeMapper.getNoticeAttach(attachNo);
    
    // 첨부 파일 File 객체 -> Resource 객체
    File file = new File(attach.getPath(), attach.getFilesystemName());
    Resource resource = new FileSystemResource(file);
    
    // 첨부 파일이 없으면 다운로드 취소
    if(!resource.exists()) {
      return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
    }

    
    // 사용자가 다운로드 받을 파일의 이름 결정(User-Agent값에 따른 인코딩 처리)
    String originalFilename = attach.getOriginalFilename();
    String userAgent = request.getHeader("User-Agent");
    try {
      // IE
      if(userAgent.contains("Trident")) {
        originalFilename = URLEncoder.encode(originalFilename, "UTF-8").replace("+", " "); // 인터넷 익스플로러는 공백이 +로 치환되기 때문에 리플레이스로 다시 변경해줌
      }
      // Edge
      else if(userAgent.contains("Edg")) {
        originalFilename = URLEncoder.encode(originalFilename, "UTF-8"); 
        
      }
      // Other
      else {
        originalFilename = new String(originalFilename.getBytes("UTF-8"), "ISO-8859-1");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    // 다운로드 응답 헤더 만들기
    HttpHeaders header = new HttpHeaders();
    header.add("Content-Type", "application/octet-stream"); // setContentType() 과 동일
    header.add("Content-Disposition", "attachment; filename=" + originalFilename);
    header.add("Content-Length", file.length() + "");  // 기본적으로 응답헤더는 스트링(빈문자열을 더해서 스트링 값으로 변환)
    
    
    // 응답
    return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
    
  }
  
  
  @Override
  public ResponseEntity<Resource> downloadAll(HttpServletRequest request) {
    
    // 다운로드 할 모든 첨부 파일 정보 가져오기
    int noticeNo = Integer.parseInt(request.getParameter("noticeNo"));
    List<NoticeAttachDto> attachList = noticeMapper.getNoticeAttachList(noticeNo);
    
    // 첨부 파일이 없으면 종료
    if(attachList.isEmpty()) {
      return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
    }
    
    // zip 파일을 생성할 경로
    File tempDir = new File(myFileUtils.getTempPath());
    if(!tempDir.exists()) {
      tempDir.mkdirs();
    }
    
    // zip 파일의 이름
    String zipName = myFileUtils.getTempFilename() + ".zip";
    
    // zip 파일의 File 객체 
    File zipFile = new File(tempDir, zipName);
    
    // zip 파일을 생성하는 출력 스트림
    ZipOutputStream zout = null;
    
    // 첨부 파일들을 순회하면서 zip 파일에 등록하기
    try {
      
      zout = new ZipOutputStream(new FileOutputStream(zipFile));
      
      for(NoticeAttachDto attach : attachList) {
        
        // 각 첨부 파일들의 원래 이름을 zip 파일에 등록하기 (이름만 등록)
        ZipEntry zipEntry = new ZipEntry(attach.getOriginalFilename());
        zout.putNextEntry(zipEntry);
        
        // 각 첨부 파일들의 내용을 zip 파일에 등록하기 (실제 파일 등록)
        BufferedInputStream bin = new BufferedInputStream(new FileInputStream(new File(attach.getPath(), attach.getFilesystemName())));
        zout.write(bin.readAllBytes());
        
        // 자원 반납
        bin.close();
        zout.closeEntry();

        
      }
      
      // zout 자원 반납
      zout.close();
      
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    // 다운로드할 zip 파일의 File 객체 -> Resource 객체
    Resource resource = new FileSystemResource(zipFile);
    
    // 다운로드 응답 헤더 만들기
    HttpHeaders header = new HttpHeaders();
    header.add("Content-Type", "application/octet-stream"); // setContentType() 과 동일
    header.add("Content-Disposition", "attachment; filename=" + zipName);
    header.add("Content-Length", zipFile.length() + "");    // 기본적으로 응답헤더는 스트링(빈문자열을 더해서 스트링 값으로 변환)
    
    
    // 응답
    return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
  }
  
  
  @Override
  public void removeTempFiles() {
    
    File tempDir = new File(myFileUtils.getTempPath()); // 임시파일 경로
    File[] targetList = tempDir.listFiles();            // 임시파일 정보 다 가져오기
    if(targetList != null) {                            // 임시파일이 하나라도 존재하면
      for(File target : targetList) {
        target.delete();                                // 임시파일 삭제하기
      }
    }
    
  }
  
}
