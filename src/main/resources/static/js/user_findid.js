
const getContextPath = () => {
  let begin = location.href.indexOf(location.host) + location.host.length;
  let end = location.href.indexOf('/', begin + 1);
  return location.href.substring(begin, end);
}


  function findId_click(){
  
    var name=$('#name').val()
    var mobile=$('#mobile').val()
    
    $.ajax({
      url: getContextPath() + "/find_id.do",
      type: "POST",
      data: "name="+name+"&mobile="+mobile,
      dataType: 'json',
      success:function(resData){  // resData = {"userNo":1, "email":"zzzzz", ...}
        if(resData.email == null){
          alert("회원 정보를 확인해주세요!");
          
        } else {
          alert(resData.email);
          
        }
      },
       error:function(){
                  alert("에러입니다");
              }
    });
  };

/*const modal = document.getElementById("modal")*/
const btnModal = document.getElementById("find_id")


btnModal.addEventListener("click", () => {
  /*console.log(modal);
   modal.style.display = "flex";*/
    findId_click();
})

    
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

