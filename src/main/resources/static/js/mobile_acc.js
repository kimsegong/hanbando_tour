// 인증번호 전송
    $(document).on('click','#goSMS', function () {    // 버튼을 클릭 했을 경우
      let userPhoneNum = $('#userPhoneNum').val();  // 사용자가 입력한 전화번호
      let authCode= $("#authCode"); //휴대폰 인증번호 담을 변수

      // 사용자가 입력한 전화번호가 공백이 아니고, 8자리 이상일 경우
      if (userPhoneNum != '' && userPhoneNum.length > 8) {
        $.ajax({
          type: 'get',
          url: getContextPath() + '/user/execute', // 요청보낼 url
          data: {'userPhoneNum': userPhoneNum}, // 사용자가 입력한 휴대폰번호 전송
          success: function (response) {
            //authCode.attr('value', response.cerNum); // authCode의 속성 value값을 인증번호로 설정
            //console.log("input태그에 담긴 인증번호: " + authCode.val()); // 확인용
            alert('인증 번호가 발송 되었습니다.');
            $('#confirmBnt').click(function() { 
              const userNum = $('#authNumber').val();
              console.log(response.cerNum)
              if(response.cerNum === userNum) {
                alert('인증 성공하였습니다.');
              }
              else {
                alert('인증 실패하였습니다. 다시 입력해주세요.');
              }
            });  
          },
          error: function(response) {
            alert('인증번호 발송에 실패하였습니다.\n잠시 후 다시 시도해주시기 바랍니다.')
          }
        });
      }else{
        alert("휴대폰 번호를 입력 해 주세요");
      }
    });
 
