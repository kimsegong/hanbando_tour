
$(() => {
	fnSearchPackageList();
	fnScroll();
})

  // 전역 변수
    var page = 1;
    var totalPage = 0;

 const fnSearchPackageList = () => {
      $.ajax({
        type: 'get',
        url: '/main/search.do', 
        data: 'page=' + page + '&search=' + $('.header_searchbar').val(),
        dataType: 'json',
        success: (resData) => {  
          totalPage = resData.totalPage;
          if (resData.packageList != null && resData.packageList.length > 0) {
            $.each(resData.sPackage, (i, package) => {           
              let str = '<div class="col-md-4">';
              str += '<div class="card">';
              str += '<img src="https://github.com/skal48/portfolio/blob/main/hanla.jpg?raw=true" style="height: 150px;" class="card-img-top" alt="Package Image">';
              str += '<div class="card-body">';
              str += '<h5 class="card-title card-font">' + package.packageTitle + '</h5>';
              str += '<p class="card-text"></p>';
              str += '<a th:href="' + '/package/increseHit.do?packageNo=' + package.packageNo + '" class="btn btn-kong">여행가기</a>';           
              str += '</div>';
              str += '</div>';
              str += '</div>';
              $('#package_list').append(str);
            });
          } else {
            console.log('데이터가 없습니다.');
          }
        },
        error: (error) => {
          console.error('Ajax 요청 에러:', error);
        }
      });
      
    };


const fnScroll = () => {
        var timerId;  // 최초 undefined 상태
        $(window).on('scroll', () => {
          if(timerId) {  // timerId가 undefined이면 false로 인식, timerId가 값을 가지면 true로 인식
            clearTimeout(timerId);
          }
          timerId = setTimeout(() => {  // setTimeout 실행 전에는 timerId가 undefined 상태, setTimeout이 한 번이라도 동작하면 timerId가 값을 가짐
              let scrollTop = $(window).scrollTop();     // 스크롤바 위치(스크롤 된 길이)
              let windowHeight = $(window).height();     // 화면 전체 크기
              let documentHeight = $(document).height(); // 문서 전체 크기
              if((scrollTop + windowHeight + 100) >= documentHeight) {  // 스크롤이 바닥에 닿기 100px 전에 true가 됨
                if(page > totalPage){  // 마지막 페이지를 보여준 이후에 true가 됨
                  return;              // 마지막 페이지를 보여준 이후에는 아래 코드를 수행하지 말 것 
                }
                page++;
                fnGetPackageList();
              }
          }, 200);  // 200밀리초(0.2초) 후 동작(시간은 임의로 조정 가능함)
        })
    }