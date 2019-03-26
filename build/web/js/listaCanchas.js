var url = "androidController";
var markers = [];
var map;
var descripcion = 'cancha';
var cant_paginas;
var pag_selec;
$(document).ready(function () {
    
     var resp= sessionStorage.getItem("complejos");
       json = $.parseJSON(resp);
     if(json){
         cargar_complejos(json);
     }
     
     
    
    $.post(url, {evento: "get_complejos"}, function (resp) {
          sessionStorage.setItem("complejos",resp);
        json = $.parseJSON(resp);
         $("#lista_complejos").html("");
        cargar_complejos(json);


    });
});

function cargar_complejos(arr) {
    $.each(arr, function (i, obj) {
        complejo_iten(obj);
    });
}

function complejo_iten(obj) {
    var html = "<div class='col-md-6 complejo_iten' onclick='ver_complejo("+JSON.stringify(obj)+");'>";
    html += "   <div style='width: 48%; height: 280px; display: inline-block; background-color: #ffffff88; '>";
    html += "                <img src='"+obj.FOTO_PERFIL+"' alt='img'/>";
    html += "  </div>";
    html += "                <div class='complejo_div' >";
    html += "                    <div class='complejo-cont'>";
    html += "                        <h3>"+obj.NOMBRE+"</h3>";
    html += "                        <p> <i class='fa fa-map-marker icon-dash' aria-hidden='true'></i>"+obj.DIRECCION+"</p>";
    html += "                        <p> <i class='fa fa-mobile icon-dash' aria-hidden='true'></i>"+obj.TELEFONO+"</p>";
    html += "                        <p> <i class='fa fa-envelope icon-dash' aria-hidden='true'></i>"+obj.CORREO+"</p>";
    html += "                    </div>";
    html += "                </div>";
    html += "            </div>";
    $("#lista_complejos").append(html);
}
function ver_complejo(obj){
    window.location.href="detallecanchas.html?id="+obj.ID;
}