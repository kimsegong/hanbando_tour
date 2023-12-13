/*email과 mobile번호가 일치하면 인증번호 전송하기*/

// Assume you have a button with the id "find_id"
const findIdButton = document.getElementById("find_id");

// Add an event listener to the button
findIdButton.addEventListener("click", () => {
  // Call the findPw_click function when the button is clicked
  
  var email=$('#email').val();
  var mobile=$('#mobile').val();
  
   
    //2.핸드폰 중복 체크 
  $.ajax({
    url: "/user/find_pw.do",
    type: "POST",
    data: "email="+email+"&mobile="+mobile,
    dataType: 'json',
    success: function(resData) { // resData = {"email":zzzzz, "mobile":"01000000", ...}
      if(resData != null && resData != "") {
        if(resData == 1){
        alert('인증번호가 발송되었습니다.');
        fnCheckSms();
         
        //window.location.href = "http://localhost:8080/user/pwCorrect.form";
        
        }
        } else if(resData == 0){
          alert("일치하는 회원 정보가 없습니다.");
        }
        }
        });
        })
      
const fnCheckSms = () => {
 
    let sms = $('#mobile').val();
    

    $.ajax({
      type: 'post',
      url: '/user/execute.form',
      data: 'userPhoneNum=' + sms,
      dataType: 'json',
      success: (resData) => {
        $('#authNumber').prop('disabled', false);
        $('#confirmBnt').prop('disabled', false);
        
        // Move the click event binding outside the AJAX success callback
        // to avoid multiple bindings.
        $('#confirmBnt').off('click').on('click', () => {
          console.log(resData.cerNum);
          //let smsPassed = $('#authNumber').val() === resData.cerNum;

          if (smsPassed = $('#authNumber').val() == resData.cerNum) {
            alert('핸드폰이 인증되었습니다.');
            $('#findForm').submit();
          } else {
            alert('핸드폰 인증이 실패했습니다.');
          }
        });
      },
      // Use the error callback to handle AJAX request errors.
      error: (jqXHR, textStatus, errorThrown) => {
        let smsPassed = false;

        switch (jqXHR.status) {
          case 400:
            $('#confirmBnt').text('인증번호 형식이 올바르지 않습니다.');
            break;
          case 403:
            $('#confirmBnt').text('이미 인증된 번호입니다.');
            break;
          default:
            // Handle other errors as needed.
            break;
        }
      }
    });
  };
