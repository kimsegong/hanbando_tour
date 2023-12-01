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

import com.tour.hanbando.dao.NoticeMapper;
import com.tour.hanbando.dto.NoticeDto;
import com.tour.hanbando.dto.ProductImageDto;
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
  
  
  
  @Override
  public int addNotice(HttpServletRequest request) {
    String title = request.getParameter("title");
    String contents = request.getParameter("contents");
    
    NoticeDto notice = NoticeDto.builder()
                     .title(title)
                     .contents(contents)
                     .build();
    
    int addResult = noticeMapper.insertNotice(notice);
    
    return addResult;
    
    
    
    
    
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
  public boolean addUpload(MultipartHttpServletRequest multipartRequest) throws Exception {
      
      String title = multipartRequest.getParameter("title");
      String contents = multipartRequest.getParameter("contents");
      int userNo = Integer.parseInt(multipartRequest.getParameter("userNo"));
      
      UploadDto upload = UploadDto.builder()
                          .title(title)
                          .contents(contents)
                          .userDto(UserDto.builder()
                                    .userNo(userNo)
                                    .build())
                          .build();
      
      int uploadCount = uploadMapper.insertUpload(upload);
      
      List<MultipartFile> files = multipartRequest.getFiles("files");
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
          
          String contentType = Files.probeContentType(file.toPath());  
          int hasThumbnail = (contentType != null && contentType.startsWith("image")) ? 1 : 0;
          
          if(hasThumbnail == 1) {
            File thumbnail = new File(dir, "s_" + filesystemName);  
            Thumbnails.of(file)
                      .size(100, 100)      
                      .toFile(thumbnail);
          }
          
          AttachDto attach = AttachDto.builder()
                              .path(path)
                              .originalFilename(originalFilename)
                              .filesystemName(filesystemName)
                              .hasThumbnail(hasThumbnail)
                              .uploadNo(upload.getUploadNo())
                              .build();
          
          attachCount += uploadMapper.insertAttach(attach);
        }
      }

      return (uploadCount == 1) && (files.size() == attachCount);
  }
 
}
