/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var url = "indexController";
        var complejo;
        $(document).ready(function () {

cargar_complejos();
});
        function cargar_complejos() {
        var id = getQueryVariable("id");
                $.post(url, {evento: "get_complejos_id", id: id}, function (resp) {
                complejo = $.parseJSON(resp);
                        $("#nombre_cancha").html(complejo.NOMBRE);
                        $("#direccion_com").html(complejo.DIRECCION);
                        $("#presentacion_com").html(complejo.PRESENTACION);
                        $("#politicas_com").html(complejo.POLITICAS);
                        if (complejo.B64 != "") {
                $("#img_perfil_com").attr("src", complejo.B64);
                }
                var telefonos = complejo.TELEFONOS;
                        var html = "";
                        $.each(telefonos, function (i, obj) {
                        html += obj.TELEFONO + "</br>";
                        });
                        $("#telefonos_com").html(html);
                        var correos = complejo.CORREOS;
                        html = "";
                        $.each(correos, function (i, obj) {
                        html += " <a href='mailto:" + obj.CORREO + "'>" + obj.CORREO + "</a>";
                        });
                        $("#correos_canchas").html(html);
                        var carrusel = complejo.FOTOS_CARRUSEL;
                        html = "";
                        $.each(carrusel, function (i, obj) {
                        html += " <div class='slide slide" + (i + 1) + "' style='background-image: url(" + obj.FOTO + "); '>";
                                html += "<div class='container'>";
                                html += "  <div class='slide-inner" + (i + 1) + " common-inner'>";
                                html += " <span class='h1 from-bottom'>Cancha Bombonera</span>";
                                html += " </div>";
                                html += " </div>";
                                html += " </div>";
                        });
                        //$("#carrusel_com").html(html);
                        var horas = complejo.HORARIOS;
                        html = "";
                        $.each(horas, function (i, obj) {
                        html += "<li>"
                                html += "<span class='pull-left'>" + get_dia(obj.DIA) + "</span>"
                                if (obj.ABIERTO == "1") {
                        html += "<span class='pull-right'>" + obj.HORA_INI + " - " + obj.HORA_FIN + "</span>"
                        } else {
                        html += "<span class='pull-right'>Cerrado</span>"
                        }

                        html += "</li>";
                        });
                        $("#lista_horarios").html(html);
                        var canchas = complejo.CANCHAS;
                        html = "";
                        $.each(canchas, function (i, obj) {
                        html += "<div class='col-md-4 col-sm-6 col-xs-12 isotopeSelector featuredItem'>";
                                html += "   <article>";
                                html += "      <figure>";
                                html += "            <img src='img/canchas/cancha1.jpg' alt='Image Protfolio' class='img-responsive'>";
                                html += "                       <div class='overlay-background'>";
                                html += "                           <div class='inner'></div>";
                                html += "                       </div>";
                                html += "                       <span class='label label-primary'>"+obj.NOMBRE+"</span>";
                                html += "                       <a href='category-grid.html'>";
                                html += "                           <div class='overlay'>";
                                html += "                               <div class='overlayInfo'>";
                                html += "                                   <span class='label label-primary'>"+obj.NOMBRE+"</span>";
                                html += "                                   <span class='label label-primary'>Reservar</span>";
                                html += "                               </div>";
                                html += "                           </div>";
                                html += "                       </a>";
                                html += "                   </figure>";
                                html += "                   <div class='figureBody'>";
                                html += "                       <h2><i class='fa fa-check-circle' aria-hidden='false'></i> Futbol 7</h2>";
                                html += "                       <h5><i class='fa fa-check-circle' aria-hidden='false'></i> Sintetica</h5>";
                                html += "                       <h5><i class='fa fa-check-circle' aria-hidden='false'></i> Techada</h5>";
                                html += "                   </div>";
                                html += "                   <div class='figureFooter'>";
                                html += "                       <div class='form-group'>";
                                html += "                           <button type='button' class='btn btn-primary' onclick='reservar_cancha("+obj.ID+")'>Reservar</button>";
                                html += "                       </div>";
                                html += "                   </div>";
                                html += "               </article>";
                                html += "           </div>";
                        });
                        $("#container").html(html);
                        initialize();
                });
        }
function getQueryVariable(varia) {
var query = window.location.search.substring(1);
        var vars = query.split("&");
        for (var i = 0; i < vars.length; i++) {
var pair = vars[i].split("=");
        if (pair[0] == varia) {
return pair[1];
        }
}
return (false);
}

function initialize() {
$(".mapa").each(function () {
var e = $(this);
        var latitud = $('#lbllatitud').html();
        var longitud = $('#lbllongitud').html();
        var descripcion = complejo.NOMBRE;
        var myLatlng = new google.maps.LatLng(complejo.LAT, complejo.LNG, 17.75);
        var mapOptions = {
        zoom: 16,
                center: myLatlng
        }

var map = new google.maps.Map(e[0], mapOptions);
        var marker = new google.maps.Marker({
        position: myLatlng,
                map: map,
                title: descripcion
                , animation: google.maps.Animation.DROP,
        });
        var popup = new google.maps.InfoWindow({
        content: descripcion
        });
        popup.open(map, marker);
        });
}

function get_dia(dia) {
switch (dia) {
case "0":
        return "Lunes";
        break;
        case "1":
        return "Martes";
        break;
        case "2":
        return "Miercoles";
        break;
        case "3":
        return "Jueves";
        break;
        case "4":
        return "Viernes";
        break;
        case "5":
        return "Sabado";
        break;
        case "6":
        return "Domingo";
        break;
        }

}

function reservar_cancha(id){
      window.location.href="reserva.html?cmp="+complejo.ID+"&ic="+id;
}
