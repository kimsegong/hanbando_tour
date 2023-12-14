package com.tour.hanbando.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.tour.hanbando.dao.PackageMapper;
import com.tour.hanbando.dto.HeartDto;
import com.tour.hanbando.dto.PackageDto;
import com.tour.hanbando.dto.ProductImageDto;
import com.tour.hanbando.dto.RegionDto;
import com.tour.hanbando.dto.ReserveDto;
import com.tour.hanbando.dto.ReviewDto;
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
  
  @Transactional(readOnly=true)
  @Override
  public Map<String, Object> getPackageList(HttpServletRequest request, String condition, int recommendStatus) {
	  Optional<String> optPage = Optional.ofNullable(request.getParameter("page"));
	    int page = Integer.parseInt(optPage.orElse("1"));
	    int display = 12;
	    int regionNo = Integer.parseInt(request.getParameter("regionNo"));	    
	    int total = packageMapper.getPackageCount(regionNo, condition);

	    myPageUtils.setPaging(page, total, display);

	    Map<String, Object> map = Map.of(
	        "begin", myPageUtils.getBegin(),
	        "end", myPageUtils.getEnd(),
	        "condition", condition,
	        "recommendStatus", recommendStatus,
	        "regionNo", regionNo
	    );
	    List<PackageDto> packageList = packageMapper.getPackageList(map);
	    return Map.of(
	        "packageList", packageList,
	        "totalPage", myPageUtils.getTotalPage()
	        ,"count", packageMapper.getPackageCount(regionNo, condition)
	    );
	}
  
  // 패키지의 ck이미지 추가하기
  @Transactional(readOnly=true)
  @Override
  public List<String> getEditorImageList(String packageContents) {
    
    List<String> editorImageList = new ArrayList<>();
        
      Document document = Jsoup.parse(packageContents);
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

    
 // 패키지 추가하기
    @Override
    public Map<String, Object> addPackage(MultipartHttpServletRequest multipartRequest) {
        Map<String, Object> result = new HashMap<>();

        String packageContents = multipartRequest.getParameter("packageContents");
        int regionNo = Integer.parseInt(multipartRequest.getParameter("regionNo"));
        int themeNo = Integer.parseInt(multipartRequest.getParameter("themeNo"));   

        // PackageDto 생성
        PackageDto packageDto = PackageDto.builder()
                .regionDto(RegionDto.builder().regionNo(regionNo).build())
                .themeDto(ThemeDto.builder().themeNo(themeNo).build())
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

        int attachCount;
        if (files.get(0).getSize() == 0) {
            attachCount = 1;
        } else {
            attachCount = 0;
        }              

        for (MultipartFile multipartFile : files) {
            if (multipartFile != null && !multipartFile.isEmpty()) {
                try {
                    String path = myPackageUtils.getUploadPath();
                    File dir = new File(path);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }

                    String filesystemName = myPackageUtils.getFilesystemName(multipartFile.getOriginalFilename());
                    File file = new File(dir, filesystemName);

                    multipartFile.transferTo(file);

                    // 변수 초기화
                    ProductImageDto attach = ProductImageDto.builder()
                            .packageNo(packageDto.getPackageNo())
                            .filesystemName(filesystemName)
                            .imagePath(path)
                            .build();

                    // 이미지 추가 결과 저장
                    attachCount += packageMapper.insertImageList(attach);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException("이미지 업로드에 실패했습니다.");
                }
            }
        }

        // 성공 시 패키지 번호를 Map에 추가
        if (addResult == 1 && files.size() == attachCount) {
            result.put("success", true);
            result.put("packageNo", packageDto.getPackageNo());
        } else {
            result.put("success", false);
            result.put("message", "패키지 추가에 실패했습니다.");
        }

        return result;
    }


  
  @Override
  public boolean addThumbnail(MultipartHttpServletRequest multipartRequest) throws Exception{
    
    String packageTitle = multipartRequest.getParameter("packageTitle");
    int packageNo = Integer.parseInt(multipartRequest.getParameter("packageNo")); 
    
    PackageDto packageDto = PackageDto.builder()
                                    .packageNo(packageNo)
                                    .packageTitle(packageTitle)
                                    .build();
    
    int updateResult = packageMapper.insertPackageTh(packageDto);
    
    List<MultipartFile> thumbnailFiles = multipartRequest.getFiles("thumbnailFiles");
    
    int attachCount;
    if(thumbnailFiles.get(0).getSize() == 0) {
      attachCount = 1;
    } else {
      attachCount = 0;
    }
    
    for(MultipartFile multipartFile : thumbnailFiles) {
      
      if(multipartFile != null && !multipartFile.isEmpty()) {
        
        String path = myPackageUtils.getUploadPath();
        File dir = new File(path);
        if(!dir.exists()) {
          dir.mkdirs();
        }
        
        String filesystemName = myPackageUtils.getFilesystemName(multipartFile.getOriginalFilename());
        File file = new File(dir, filesystemName);
        
        multipartFile.transferTo(file);
                     
        String contentType = Files.probeContentType(file.toPath());  // 이미지의 Content-Type은 image/jpeg, image/png 등 image로 시작한다.
        int hasthumbnail = (contentType != null && contentType.startsWith("image")) ? 1 : 0;
        
        if(hasthumbnail == 1) {
          File thumbnail = new File(dir, "s_" + filesystemName);  
          Thumbnails.of(file)
                    .size(100, 100)     
                    .toFile(thumbnail);
        }
                
        ProductImageDto packageImage = ProductImageDto.builder()
            .packageNo(packageNo)
            .filesystemName(filesystemName)
            .imagePath(path)
            .thumbnail(hasthumbnail)
            .build();
                      
        attachCount += packageMapper.insertImageList(packageImage);
        
        }  // if
        
        }  // for
        return (updateResult == 1) && (thumbnailFiles.size() == attachCount);
                

  }
  

  // 지역/테마 넣기
  @Override
  public int addRegion(HttpServletRequest request) {
        String regionName = request.getParameter("regionName");
        
        RegionDto regionDto = RegionDto.builder()
                              .regionName(regionName)
                              .build();
        
        int regionResult = packageMapper.insertRegion(regionDto);
        
        return regionResult;
    }
  
   @Override
   public int addTheme(HttpServletRequest request) {
        String themeName = request.getParameter("themeName");     
        
        ThemeDto themeDto = ThemeDto.builder()
                             .themeName(themeName)
                             .build();
        
        int themeResult = packageMapper.insertTheme(themeDto);
        
        return themeResult;
    }

  
    // 패키지 상세보기
    @Transactional(readOnly=true)
    @Override
    public PackageDto getPackage(int packageNo) {
      return packageMapper.getPackage(packageNo);
    }
    
    @Override
    public Map<String, Object> imageUpload(MultipartHttpServletRequest multipartRequest) {
        // 이미지가 저장될 경로
        String imagePath = myPackageUtils.getPackageImagePath();
        File dir = new File(imagePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 이미지 파일 (CKEditor는 이미지를 upload라는 이름으로 보냄)
        MultipartFile upload = multipartRequest.getFile("upload");

        // 이미지가 저장될 이름
        String filesystemName = myPackageUtils.getFilesystemName(upload.getOriginalFilename());

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
    
    // 수정
    @Override
    public int modifyPackage(HttpServletRequest request) {
              
        int packageNo = Integer.parseInt(request.getParameter("packageNo"));
        int regionNo = Integer.parseInt(request.getParameter("regionNo"));
        int themeNo = Integer.parseInt(request.getParameter("themeNo"));
        String packageContents = request.getParameter("packageContents");
          
        List<ProductImageDto> packageImageDtoList = packageMapper.getPackageImageList(packageNo);
        List<String> packageImageList = packageImageDtoList.stream()
                                      .map(packageImageDto -> packageImageDto.getFilesystemName())
                                      .collect(Collectors.toList());
            
        // Editor에 포함된 이미지 이름(filesystemName)
        List<String> editorImageList = getEditorImageList(packageContents);

        // Editor에 포함되어 있으나 기존 이미지에 없는 이미지는 IMAGE_T에 추가해야 함
        editorImageList.stream()
          .filter(editorImage -> !packageImageList.contains(editorImage))        
          .map(editorImage -> ProductImageDto.builder()                      
                                .packageNo(packageNo)
                                .imagePath(myPackageUtils.getPackageImagePath())
                                .filesystemName(editorImage)
                                .build())
          .forEach(packageImageDto -> packageMapper.insertPackageImage(packageImageDto)); 
             
        // 기존 이미지에 있으나 Editor에 포함되지 않은 이미지는 삭제해야 함
        List<ProductImageDto> removeList = packageImageDtoList.stream()
                                          .filter(packageImageDto -> !editorImageList.contains(packageImageDto.getFilesystemName())) 
                                          .collect(Collectors.toList());                                                       

        for(ProductImageDto packageImageDto : removeList) {
          // IMAGE_T에서 삭제
          packageMapper.deletePackageImage(packageImageDto.getFilesystemName());  // 파일명은 UUID로 만들어졌으므로 파일명의 중복은 없다고 생각하면 된다.
          // 파일 삭제
          File file = new File(packageImageDto.getImagePath(), packageImageDto.getFilesystemName());
          if(file.exists()) {
            file.delete();
          }
        }
        
        
        PackageDto packageDto = PackageDto.builder()
                              .packageNo(packageNo)
                              .regionDto(RegionDto.builder().regionNo(regionNo).build())
                              .themeDto(ThemeDto.builder().themeNo(themeNo).build())
                              .packageTitle(request.getParameter("packageTitle"))
                              .miniOne(request.getParameter("miniOne"))
                              .miniTwo(request.getParameter("miniTwo"))
                              .miniThree(request.getParameter("miniThree"))
                              .packagePlan(request.getParameter("packagePlan"))
                              .packageContents(packageContents)                  
                              .hotelContents(request.getParameter("hotelContents"))
                              .price(Integer.parseInt(request.getParameter("price")))
                              .danger(request.getParameter("danger"))
                              .maxPeople(Integer.parseInt(request.getParameter("maxPeople")))
                              .recommendStatus(Integer.parseInt(request.getParameter("recommendStatus")))
                              .build();
        
        
        int modifyResult = packageMapper.updatePackage(packageDto);
        return modifyResult;
        
    } 
    
    
    @Override
    public Map<String, Object> getAttachList(HttpServletRequest request) {
      
      Optional<String> opt = Optional.ofNullable(request.getParameter("packageNo"));
      int packageNo = Integer.parseInt(opt.orElse("0"));
      return Map.of("attachList", packageMapper.getPackageImageList(packageNo));
     
    }   
    
    @Override
    public void loadUpload(HttpServletRequest request, Model model) {
        
        Optional<String> opt = Optional.ofNullable(request.getParameter("packageNo"));
        int packageNo = Integer.parseInt(opt.orElse("0"));
        
        model.addAttribute("package", packageMapper.getPackage(packageNo));
        model.addAttribute("attachList", packageMapper.getPackageImageList(packageNo));
    }

    
    // 조회수
    @Transactional(readOnly=true)
    @Override
    public int increseHit(int packageNo) {
      return packageMapper.packageHit(packageNo);
    }
    // 조회수 불러오기
    @Transactional(readOnly=true)
    @Override
    public Map<String, Object> getHit(HttpServletRequest request) {
          
       Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
            int page = Integer.parseInt(opt.orElse("1"));
            int total = packageMapper.getCount();
            int display = 9;

            myPageUtils.setPaging(page, total, display);

            Map<String, Object> map = Map.of("begin", myPageUtils.getBegin(),
                                            "end", myPageUtils.getEnd());

            List<PackageDto> hitList = packageMapper.getHitList(map);
            return Map.of("hitList", hitList,
                          "totalPage", myPageUtils.getTotalPage());
    }
    @Transactional(readOnly=true)
    @Override
    public void getRegionAndTheme(HttpServletRequest request, Model model) {
      
        String regionNo = request.getParameter("regionNo");
        String themeNo = request.getParameter("themeNo");

        
        Map<String, Object> map = new HashMap<>();
        
        map.put("regionNo", regionNo);
        map.put("themeNo", themeNo);
      
        List<RegionDto> regionList = packageMapper.getRegion(map);
        List<ThemeDto> themeList = packageMapper.getTheme(map);
 
        
        model.addAttribute("regionList", regionList);
        model.addAttribute("themeList", themeList);
    }
     
    
    // 패키지 삭제
    @Override
    public int removePackage(int packageNo) {   
        
      List<ProductImageDto> productImageList = packageMapper.getPackageImageList(packageNo);
        for(ProductImageDto productImage : productImageList) {
          File file = new File(productImage.getImagePath(), productImage.getFilesystemName());
          if(file.exists()) {
            file.delete();
          }
        }     
        
        packageMapper.deletePackageImageList(packageNo);
        
        return packageMapper.deletePackage(packageNo);
    }
    
    // 리뷰넣기
    @Override
    public Map<String, Object> addReview(HttpServletRequest request) {
    
      String reviewContents = request.getParameter("reviewContents");
      int userNo = Integer.parseInt(request.getParameter("userNo"));
      int packageNo = Integer.parseInt(request.getParameter("packageNo"));
      int star = Integer.parseInt(request.getParameter("star"));
      
      ReviewDto review = ReviewDto.builder()                            
                            .reviewContents(reviewContents)
                            .packageNo(packageNo)
                            .star(star)
                            .userDto(UserDto.builder()
                                      .userNo(userNo)
                                      .build())                            
                            .build();

      int addReviewResult = packageMapper.insertReview(review);
      
      return Map.of("addReviewResult", addReviewResult);
      
    }
    
    // 리뷰 리스트 
    @Transactional(readOnly=true)
    @Override
    public Map<String, Object> loadReviewList(HttpServletRequest request) {
    
      
      int packageNo = Integer.parseInt(request.getParameter("packageNo"));
      
      String pageParameter = request.getParameter("page");
      int page = 1;  // 기본값 설정
      if (pageParameter != null && !pageParameter.isEmpty()) {
          try {
              page = Integer.parseInt(pageParameter);
          } catch (NumberFormatException e) {             
              e.printStackTrace();  
          }
      }
      int total = packageMapper.getReviewCount(packageNo);
      int display = 10;
      
      myPageUtils.setPaging(page, total, display);
      
      Map<String, Object> map = Map.of("packageNo", packageNo
                                     , "begin", myPageUtils.getBegin()
                                     , "end", myPageUtils.getEnd());
      
      List<ReviewDto> reviewList = packageMapper.getReviewList(map);
      String paging = myPageUtils.getAjaxPaging();
      Map<String, Object> result = new HashMap<String, Object>();
      result.put("reviewList", reviewList);
      result.put("paging", paging);
      return result;
      
    }
      
	@Transactional(readOnly=true)
	@Override
    public int getTotalReviewCount(int packageNo) {
        return packageMapper.getReviewCount(packageNo);
    }
	
    @Override
    public Map<String, Object> loadReviewStarList(HttpServletRequest request) {
       int packageNo = Integer.parseInt(request.getParameter("packageNo"));
      
      String pageParameter = request.getParameter("page");
      int page = 1;  // 기본값 설정
      if (pageParameter != null && !pageParameter.isEmpty()) {
          try {
              page = Integer.parseInt(pageParameter);
          } catch (NumberFormatException e) {             
              e.printStackTrace();  
          }
      }
      int total = packageMapper.getReviewCount(packageNo);
      int display = 10;
      
      myPageUtils.setPaging(page, total, display);
      
      Map<String, Object> map = Map.of("packageNo", packageNo
                                     , "begin", myPageUtils.getBegin()
                                     , "end", myPageUtils.getEnd());
      
      List<ReviewDto> reviewList = packageMapper.getReviewStarList(map);
      String paging = myPageUtils.getAjaxPaging();
      Map<String, Object> result = new HashMap<String, Object>();
      result.put("reviewList", reviewList);
      result.put("paging", paging);
      return result;
    }
    
    @Override
    public double getAverageRating(int packageNo) {
      return packageMapper.starAve(packageNo);
    }
    
    @Transactional(readOnly=true)
    @Override
    public List<ReserveDto> getReserveUser(int packageNo) {
        List<ReserveDto> reserve = packageMapper.getReserve(packageNo);
        return reserve ;
    }
    
    @Override
    public Map<String, Object> removeReview(int reviewNo) {
        int removeResult = packageMapper.deleteReview(reviewNo);
        return Map.of("removeResult", removeResult);
      }
    
    @Override
    public int addHeart(HttpServletRequest request) {
    
      int userNo = Integer.parseInt(request.getParameter("userNo"));
      int packageNo = Integer.parseInt(request.getParameter("packageNo"));

      HeartDto heart = HeartDto.builder()
              .packageDto(PackageDto.builder()
                          .packageNo(packageNo)
                          .build())
              .userDto(UserDto.builder().userNo(userNo).build())
                  .build();
    return packageMapper.heartProduct(heart);
    }
    
    
    @Override
    public void getHeartPackage(HttpServletRequest request, Model model) {
      
      
      Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
      int page = Integer.parseInt(opt.orElse("1"));
      int display = 10;
      int userNo = Integer.parseInt(request.getParameter("userNo"));
      int total = packageMapper.getHeartCount(userNo);
      
      myPageUtils.setPaging(page, total, display);
      
      Map<String, Object> map = Map.of("begin", myPageUtils.getBegin()
                                     , "end", myPageUtils.getEnd()
                                     , "userNo", userNo);
      
      List<HeartDto> heartList = packageMapper.selectHeartList(map);

      model.addAttribute("heartList", heartList);
      String params = "userNo=" + request.getParameter("userNo");
      model.addAttribute("paging", myPageUtils.getMvcPaging(request.getContextPath() + "/user/heart.do", params));
      model.addAttribute("beginNo", total - (page - 1) * display); 

    }
    
    @Override
    public Map<String, Object> removeHeart(int packageNo) {
        int removeHeartResult = packageMapper.deleteHeart(packageNo);
        return Map.of("removeHeartResult", removeHeartResult);
      }
    
    @Transactional(readOnly=true)
    @Override
    public Map<String, Object> checkHeart(int packageNo, int userNo) {
        Map<String, Object> map = new HashMap<>();
        map.put("packageNo", packageNo);
        map.put("userNo", userNo);

        int heartCount = packageMapper.checkHeart(map);

        boolean enableHeart = heartCount > 0;

        return Map.of("enableHeart", enableHeart);
    }
}
