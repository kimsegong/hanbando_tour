package com.tour.hanbando.service;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.tour.hanbando.dao.PackageMapper;
import com.tour.hanbando.dto.PackageDto;
import com.tour.hanbando.dto.ProductImageDto;
import com.tour.hanbando.dto.RegionDto;
import com.tour.hanbando.dto.ThemeDto;
import com.tour.hanbando.dto.UserDto;
import com.tour.hanbando.util.MyPackageUtils;
import com.tour.hanbando.util.MyPageUtils;

import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;

@RequiredArgsConstructor
@Service
public class PackageServiceImpl implements PackageService {

  private final PackageMapper packageMapper;
  private final MyPageUtils myPageUtils;
  private final MyPackageUtils myPackageUtils;
  
  @Override
  public Map<String, Object> getPackageList(HttpServletRequest request) {
    Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
    int page = Integer.parseInt(opt.orElse("1"));
    int total = packageMapper.getPackageCount();
    int display = 9;
    
    myPageUtils.setPaging(page, total, display);
    
    Map<String, Object> map = Map.of("begin", myPageUtils.getBegin()
                                   , "end", myPageUtils.getEnd());
              
    List<PackageDto> packageList = packageMapper.getPackageList(map);
    return Map.of("packageList", packageList
                , "totalPage", myPageUtils.getTotalPage());
    
  }
  
  @Transactional(readOnly=true)
  @Override
  public List<String> getEditorImageList(String contents) {
    
    List<String> editorImageList = new ArrayList<>();
        
      Document document = Jsoup.parse(contents);
      Elements elements =  document.getElementsByTag("img");
        
     if(elements != null) {
       for(Element element : elements) {
         String src = element.attr("src");
         String filesystemName = src.substring(src.lastIndexOf("/") + 1);
          editorImageList.add(filesystemName);
      }
     }
        
   return editorImageList;
  }
  
  @Override
  public int addPackage(MultipartHttpServletRequest multipartRequest) throws Exception {
      String packageContents = multipartRequest.getParameter("packageContents");

      try {
          String userNoStr = multipartRequest.getParameter("userNo");
          if (userNoStr == null || userNoStr.isEmpty()) {
              return 0; // Invalid user number
          }

          int userNo = Integer.parseInt(userNoStr);

          // 다른 파라미터들에 대한 유사한 방어 코드 추가

          // PackageDto 생성
          PackageDto packageDto = PackageDto.builder()
              .userDto(UserDto.builder()
                            .userNo(userNo)
                            .build())
                  .regionDto(RegionDto.builder().regionNo(1).build())
                  .themeDto(ThemeDto.builder().themeNo(1).build())
                  .packageTitle(multipartRequest.getParameter("packageTitle"))
                  .miniOne(multipartRequest.getParameter("miniOne"))
                  .miniTwo(multipartRequest.getParameter("miniTwo"))
                  .miniThree(multipartRequest.getParameter("miniThree"))
                  .packagePlan(multipartRequest.getParameter("packagePlan"))
                  .packageContents(packageContents)                  
                  .hotelContents(multipartRequest.getParameter("hotelContents"))
                  .price(Integer.parseInt(multipartRequest.getParameter("price")))
                  .danger(multipartRequest.getParameter("danger"))
                  .maxPeople(Integer.parseInt(multipartRequest.getParameter("maxPeople")))
                  .recommendStatus(Integer.parseInt(multipartRequest.getParameter("recommendStatus")))
                  .build();

          int addResult = packageMapper.insertPackage(packageDto);

          // CKEditor 이미지 처리
          List<String> editorImages = getEditorImageList(packageContents);
          for (String editorImage : editorImages) {
              ProductImageDto packageImage = ProductImageDto.builder()                      
                      .packageNo(packageDto.getPackageNo())
                      .filesystemName(editorImage)
                      .imagePath(myPackageUtils.getPackageImagePath())                      
                      .build();
              packageMapper.insertPackageImage(packageImage);
          }

          // 파일 업로드 및 이미지 처리
          List<MultipartFile> files = multipartRequest.getFiles("files");

          int thumbnailCount = 0;
          for (MultipartFile multipartFile : files) {
              if (multipartFile != null && !multipartFile.isEmpty()) {
                  String path = myPackageUtils.getUploadPath();
                  File dir = new File(path);
                  if (!dir.exists()) {
                      dir.mkdirs();
                  }

                  String filesystemName = myPackageUtils.getFilesystemName(multipartFile.getOriginalFilename());
                  File file = new File(dir, filesystemName);

                  multipartFile.transferTo(file);

                  String contentType = Files.probeContentType(file.toPath());
                  int thumbnail = (contentType != null && contentType.startsWith("image")) ? 1 : 0;

                  if (thumbnail == 1) {
                      File thumbnailFile = new File(dir, "s_" + filesystemName);
                      Thumbnails.of(file).size(100, 100).toFile(thumbnailFile);
                  }

                  try {
                      ProductImageDto attach = ProductImageDto.builder()
                              .packageNo(packageDto.getPackageNo())
                              .thumbnail(thumbnail)
                              .filesystemName(filesystemName)
                              .imagePath(path)
                              .build();

                      thumbnailCount += packageMapper.insertThumbnail(attach);

                  } catch (Exception e) {
                      e.printStackTrace();

                  }
              }
          }

          // 성공 시 1, 실패 시 0 반환
          return addResult == 1 && files.size() == thumbnailCount ? 1 : 0;
      } catch (NumberFormatException e) {
          e.printStackTrace();
          return 0; // Invalid number format
      }
  }
}
