package com.tour.hanbando.service;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;

import com.gdu.myhome.dto.AttachDto;
import com.tour.hanbando.dao.NoticeMapper;
import com.tour.hanbando.dto.NoticeAttachDto;
import com.tour.hanbando.dto.NoticeDto;
import com.tour.hanbando.dto.PackageDto;
import com.tour.hanbando.dto.ProductImageDto;
import com.tour.hanbando.dto.RegionDto;
import com.tour.hanbando.dto.ThemeDto;
import com.tour.hanbando.dto.UserDto;
import com.tour.hanbando.util.MyFileUtils;
import com.tour.hanbando.util.MyNoticeUtils;
import com.tour.hanbando.util.MyPackageUtils;
import com.tour.hanbando.util.MyPageUtils;

import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;

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
  public NoticeDto loadNotice(int noticeNo) {
    return noticeMapper.getNotice(noticeNo);
  }
  
  @Override
  public int modifyNotice(HttpServletRequest request) {
 // 수정할 제목/내용/블로그번호
    String title = request.getParameter("title");
    String contents = request.getParameter("contents");
    int noticeNo = Integer.parseInt(request.getParameter("noticeNo"));
// 수정할 제목/내용/블로그번호를 가진 BlogDto
    NoticeDto notice = NoticeDto.builder()
                    .noticeNo(noticeNo)
                    .title(title)
                    .contents(contents)
                    .build();
    
    // BLOG_T 수정
    int modifyResult = noticeMapper.updateNotice(notice);
    
    return modifyResult;
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
                           .noticeNo(Integer.parseInt(multipartRequest.getParameter("noticeNo")))
                           .build())
              .build();

          // 이미지 추가 결과 저장
          attachCount += noticeMapper.insertNoticeAttach(attach);
        }
      }         

    // 성공 시 1, 실패 시 0 반환
    return (noticeCount == 1) && (files.size() == attachCount);
  }
  
  
  @Override
  public Map<String, Object> imageUpload(MultipartHttpServletRequest multipartRequest) {
      // 이미지가 저장될 경로
      String imagePath = myNoticeUtils.getNoticeImagePath();
      File dir = new File(imagePath);
      if (!dir.exists()) {
          dir.mkdirs();
      }
      
      

      // 이미지 파일 (CKEditor는 이미지를 upload라는 이름으로 보냄)
      MultipartFile upload = multipartRequest.getFile("upload");

      // 이미지가 저장될 이름
      String filesystemName = myNoticeUtils.getFilesystemName(upload.getOriginalFilename());

      // 이미지 File 객체
      File file = new File(dir, filesystemName);
      // 저장
      try {
          upload.transferTo(file);
      } catch (Exception e) {
          e.printStackTrace();
      }
      // CKEditor로 저장된 이미지의 경로를 JSON 형식으로 반환해야 함
      return Map.of("uploaded", true
              , "url", multipartRequest.getContextPath() + imagePath + "/" + filesystemName);
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
        
        String contentType = Files.probeContentType(file.toPath());  // 이미지의 Content-Type은 image/jpeg, image/png 등 image로 시작한다.
        

        
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
    return null;
  }
  
}
