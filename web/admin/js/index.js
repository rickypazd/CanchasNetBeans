/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var usr_log;
$(document).ready(function () {

    if (!sessionStorage.getItem("usr_log")) {
        location.href = "login.html";
    }
    usr_log = $.parseJSON(sessionStorage.getItem("usr_log"));

    $(".admin_iten").css('display', 'none');
    $(".usr_iten").css('display', 'none');
    if (usr_log.ROL == 1) {
        cargar_admin();
    } else if (usr_log.ROL == 2) {
        cargar_usr();
    }

});
function cargar_admin() {
    $(".admin_iten").css('display', '');
    $(".nombre_usr").html(usr_log.USUARIO);
}

function change_frame(frame) {
    $("#frame_ini").attr("src", frame + ".html");


}
function resizes(heig) {
    $("#frame_ini").css("height", heig + 10);
    $(".conten-frame").css("height", heig + 10);
    window.focus();
}
function desconectarse() {
    sessionStorage.removeItem("usr_log");
    location.href = "login.html";
}

function cargar_usr() {
      $(".usr_iten").css('display','');
    $(".nombre_usr").html(usr_log.NOMBRE+" "+usr_log.APELLIDO);
}