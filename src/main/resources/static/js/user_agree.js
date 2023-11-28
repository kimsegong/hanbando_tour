/**
 * 가입 이전 단계 : 약관 동의 페이지
 */

$(() => {
  fnChkAll();
  fnChkEach();
  fnJoinForm();
})

// 전체 선택을 클릭하면 개별 선택에 영향을 미친다.
const fnChkAll = () => {
  $('#chk_all').click((ev) => {
    $('.chk_each').prop('checked', $(ev.target).prop('checked'));
  })
}

// 개별 선택을 클릭하면 전체 선택에 영향을 미친다.
const fnChkEach = () => {
  $(document).on('click', '.chk_each', () => {
    var total = 0;
    $.each($('.chk_each'), (i, elem) => {
      total += $(elem).prop('checked');
    })
    $('#chk_all').prop('checked', total === $('.chk_each').length);
  })
}

// 필수 동의를 해야만 가입 페이지로 이동할 수 있다.
const fnJoinForm = () => {
  $('#frm_agree').submit((ev) => {
    if(!$('#service').is(':checked')){
      alert('필수 약관에 동의하세요.');
      ev.preventDefault();
      return;
    }
  })
}