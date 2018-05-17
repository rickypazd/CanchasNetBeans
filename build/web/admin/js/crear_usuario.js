/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var url = "adminController";
function registrar() {
    var nombre = $("#nombre").val() || 0;
    var apellidos = $("#apellidos").val() || 0;
    var telefono = $("#telefono").val() || 0;
    var email = $("#email").val() || 0;
    var usuario = $("#usuario").val() || 0;
    var password = $("#password").val() || 0;
    var rep_password = $("#rep_password").val() || 0;

      $("#nombre").css("background", "#ffffff");
      $("#apellidos").css("background", "#ffffff");
      $("#telefono").css("background", "#ffffff");
      $("#email").css("background", "#ffffff");
      $("#usuario").css("background", "#ffffff");
      $("#password").css("background", "#ffffff");
      $("#rep_password").css("background", "#ffffff");
  
    if (nombre <= 0) {
        $("#nombre").css("background", "#F09C84");
        $("#nombre").focus();
        return;
    }
    if (apellidos <= 0) {
        $("#apellidos").css("background", "#F09C84");
        $("#apellidos").focus();
        return;
    }
    if (telefono <= 0) {
        $("#telefono").css("background", "#F09C84");
        $("#telefono").focus();
        return;
    }
    if (email <= 0) {
        $("#email").css("background", "#F09C84");
        $("#email").focus();
        return;
    }
    if (usuario <= 0) {
        $("#usuario").css("background", "#F09C84");
        $("#usuario").focus();
        return;
    }
    if (password <= 0) {
        $("#password").css("background", "#F09C84");
        $("#password").focus();
        return;
    }
    if (rep_password <= 0) {
        $("#rep_password").css("background", "#F09C84");
        $("#rep_password").focus();
        return;
    }
    if (password !== rep_password) {
       $("#rep_password").css("background", "#F09C84");
        $("#rep_password").focus();
        return;
    }
    $.post(url,{evento:"crear_usuario",
                nombre:nombre,
                apellidos:apellidos,
                telefono:telefono,
                password:password,
                usuario:usuario,
                email:email
                },function(resp){
                    if(resp=="valido"){
                        alert("USUARIO CREADO");
                        change_frame_padre("inicio");
                    }else{
                        alert("USUARIO NO CREADO, ocurrio algun error, intente cambiar el nombre de usuario");
                    }
    });
}

function change_frame_padre(dir) {
    window.parent.change_frame(dir);
}