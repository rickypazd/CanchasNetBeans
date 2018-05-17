/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var url = "adminController";

function logear(){
    var usr=$("#inputUsuario").val() || 0;
    var pass=$("#inputPassword").val() || 0;
    
    if(usr==0){
        return;
    }
    
    if(pass==0){
        return;
    }
    $.post(url,{evento:"iniciar_session",
                usr:usr,
                pass:pass},function(resp){
        if(resp=="error"){
            
        }else{
            sessionStorage.setItem("usr_log",resp);
              location.href="index.html";      
              
        }
    });
      
}