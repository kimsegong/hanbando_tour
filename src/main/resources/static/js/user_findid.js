$(() => {
  fnCheckPwMobile();
})





  const fnCheckPwMobile = () => {
    var name = $('#name').val()
    var mobile = $('#mobile').val()
    $('#find_id').click(() => {
      $.ajax({
        url: getContextPath() + "/find_id.do",
        type: "POST",
        data: "name="+name+"&mobile="+mobile,
        dataType: 'json',
        success:function(resData){  // resData = {"userNo":1, "email":"zzzzz", ...}
          if(resData.mobile == null){
            alert("회원 정보를 확인해주세요!");
            alert(resData.mobile);
          } else {
            alert(resData.mobile);
          }
        },
        error:function(){
          alert("에러입니다");
        }
      })
    })
  }
  

    
/*const closeBtn = modal.querySelector(".close-area")
closeBtn.addEventListener("click", () => {
   modal.style.display = "none"
})

modal.addEventListener("click", (e) => {
    const evTarget = e.target
   if(evTarget.classList.contains("modal-overlay")) {
       modal.style.display = "none"
   }
})
*/
