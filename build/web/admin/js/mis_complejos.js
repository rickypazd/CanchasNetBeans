/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


var url = "adminController";
$(document).ready(function () {
    $(document).scroll(evt_scroll);
    cargar_complejos();
});

function cargar_complejos() {
    var usr_loge = $.parseJSON(sessionStorage.getItem("usr_log"));
    var id = usr_loge.ID;
    var html = "";
    var url_img = "";
    $.post(url, {evento: "get_complejos_usr", id: id}, function (resp) {
        var arr = $.parseJSON(resp);
        $.each(arr, function (i, obj) {
            url_img = "img/sin-imagen.png";
            html += " <div class='row view_admin_comple'>";
            html += "      <div class='col-sm-5 col-xs-12'>";
            html += "   <div class='categoryImage'>";
            if (obj.FOTO_PERFIL != "") {
                url_img = obj.FOTO_PERFIL + "";
            }
            html += "        <img src='../" + url_img + "' alt='Image category' class='img-responsive img-rounded'>";
            html += "    </div>";
            html += " </div>";
            html += " <div class='col-sm-7 col-xs-12'>";
            html += " <div class='categoryDetails'>";
            html += "     <h2>";
            html += "           <a href='#' style='color:#222222' onclick='ver_cancha(" + obj.ID + ")'>" + obj.NOMBRE + "</a>";
            html += "      </h2>";
            html += "      <p>";
            html += "          <i class='icon-listy icon-target' style='color: #1bac06;'></i>";
            html += "         <span class='placeName'>" + obj.DIRECCION + "</span>";
            html += "     </p>";
            html += "     <p style='text-align:justify'>" + obj.PRESENTACION + "</p>";
            html += "    <ul class='list-inline list-tag'>";
            html += "       <li><button type='button' class='btn_verde' onclick='ver_cancha(" + obj.ID + ")'>editar </button></li>";
            html += "   </ul>";
            html += "  </div>";
            html += "  </div>";
            html += "  </div>";
        });
        $("#complejos_view_con").html(html);
        rezizar();
        rezizar();
    });
}
function ver_cancha(id) {
    sessionStorage.setItem("id_cancha", id);
     location.href = "ver_cancha_admin.html";
}
function change_frame_padre(dir) {
    window.parent.change_frame(dir);
}

function evt_scroll(event) {
    rezizar();
}
function rezizar() {
    var h = $(document).height();
    window.parent.resizes(h + 15);
}
