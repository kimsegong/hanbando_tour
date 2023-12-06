/**
 * 회원 가입 페이지
 */
/* 전역변수 선언 */
var emailPassed = false;
var pwPassed = false;
var pw2Passed = false;
var namePassed = false;
var mobilePassed = false;
var smsPassed = false;


/* 함수 정의 */

/*인증번호 입력 후 확인하는 곳*/



const fnCheckSms = () => {
  $('#goSMS').click(() => {
    let sms = $('#userPhoneNum').val();
    alert("인증번호가 발송되었습니다.");

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
  });
};
//이메일 -> 아이디로 쓰기 

function fnValidUser() {
  let validFlag = true;
  
  if (!emailPassed) {
    alert('이메일 체크를 해주세요.');
    return false;
  }
  
  if(!pwPassed) {
    alert('패스워드 확인을 해주세요.');
    return false;
  }
  
  if (validFlag) {
    $('#frm_join').submit();
  }
}

function fnCheckEmail() {
  console.log('btn_get_code');
  let email = $('#email').val();

  // 1. 정규식 검사
  let regEmail = /^[A-Za-z0-9-_]+@[A-Za-z0-9]{2,}([.][A-Za-z]{2,6}){1,2}$/;
  if (!regEmail.test(email)) {
    $('#msg_email').text('이메일 형식이 올바르지 않습니다.');
    return;
  }

  // 2. 이메일 중복 체크
  $.ajax({
    // 요청
    type: 'get',
    url: '/user/checkEmail.do',
    data: 'email=' + email,
    // 응답
    dataType: 'json',
    success: (resData) => {  // resData === {"enableEmail": true}
      if (resData.enableEmail) {
        console.log('resolve');
        $('#msg_email').text('');
        if(!emailPassed){
          alert('해당 아이디를 사용할 수 있습니다.');
          return;
        }
    
      } else {
        console.log('reject');
        $('#msg_email').text('이미 가입한 이메일입니다. 다른 이메일을 입력해 주세요.');
      }
    } 
  });

  

}

/* 함수 정의 */


/*$('#btn_get_code').click(() => {
  
  // 연속된 ajax() 함수 호출의 실행 순서를 보장하는 JavaScript 객체 Promise
  new Promise((resolve, reject) => {
    

    
  })
  .then(() => {
    // 성공적으로 이메일 체크가 완료된 경우의 로직
    // 이 부분에 추가로 실행할 코드를 작성하세요.
      
    console.log('then');
      }).catch((state) => {
    // 실패한 경우의 로직
    emailPassed = false;
    switch (state) {
      case 1:
        
        break;
      case 2:
        
        break;
    }
  })
  });
*/












/////
const fnCheckPassword = () => {
  $('#pw').keyup((ev) => {
    let pw = $(ev.target).val();
    // 비밀번호 : 8~20자, 영문,숫자,특수문자, 2가지 이상 포함
    let validPwCount = /[A-Z]/.test(pw)          // 대문자가 있으면   true
                     + /[a-z]/.test(pw)          // 소문자가 있으면   true
                     + /[0-9]/.test(pw)          // 숫자가 있으면     true
                     + /[^A-Za-z0-9]/.test(pw);  // 특수문자가 있으면 true
    pwPassed = pw.length >= 8 && pw.length <= 20 && validPwCount >= 2;
    if(pwPassed){
      $('#msg_pw').text('사용 가능한 비밀번호입니다.');
    } else {
      $('#msg_pw').text('비밀번호는 8~20자, 영문/숫자/특수문자를 2가지 이상 포함해야 합니다.');       
    }
  })
}

const fnCheckPassword2 = () => {
  $('#pw2').blur((ev) => {
    let pw = $('#pw').val();
    let pw2 = ev.target.value;
    pw2Passed = (pw !== '') && (pw === pw2);
    if(pw2Passed){
      $('#msg_pw2').text('');
    } else {
      $('#msg_pw2').text('비밀번호 입력을 확인하세요.');
    }
  })
}

const fnCheckName = () => {
  $('#name').blur((ev) => {
    let name = ev.target.value;
    let bytes = 0;
    for(let i = 0; i < name.length; i++){
      if(name.charCodeAt(i) > 128){  // 코드값이 128을 초과하는 문자는 한 글자 당 2바이트임
        bytes += 2;
      } else {
        bytes++;
      }
    }
    namePassed = (bytes <= 50);
    if(!namePassed){
      $('#msg_name').text('이름은 50바이트 이내로 작성해야 합니다.');
    }
  })
}

const fnCheckMobile = () => {
  $('#mobile').keyup((ev) => {
    ev.target.value = ev.target.value.replaceAll('-', '');
    // 휴대전화번호 검사 정규식 (010숫자8개)
    let regMobile = /^010[0-9]{8}$/;
    mobilePassed = regMobile.test(ev.target.value);
    if(mobilePassed){
      $('#msg_mobile').text('');
    } else {
      $('#msg_mobile').text('휴대전화번호를 확인하세요.');       
    }
  })
}

const fnJoin = () => {
  $('#frm_join').submit((ev) => {
    if(!emailPassed){
      alert('이메일을 인증 받아야 합니다.');
      ev.preventDefault();
      return;
    } else if(!pwPassed || !pw2Passed){
      alert('비밀번호를 확인하세요.');
      ev.preventDefault();
      return;
    } else if(!namePassed){
      alert('이름을 확인하세요.');
      ev.preventDefault();
      return;
    } else if(!mobilePassed){
      alert('휴대전화번호를 확인하세요.');
      ev.preventDefault();
      return;
    }
  })
}
