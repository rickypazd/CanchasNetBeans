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
        var caract = complejo.CARACTERISTICAS;
        var html = "";
        $.each(caract, function (i, obj) {
            html += " <li><i class='fa fa-check-circle-o' aria-hidden='true'></i>" + obj.CARACTERISTICA + "</li>";
        });
        $("#caracteristicas").html(html);
        var correos = complejo.CORREOS;
        html = "";
        $.each(correos, function (i, obj) {
            html += " <a href='mailto:" + obj.CORREO + "'>" + obj.CORREO + "</a>";
        });
        $("#correos_canchas").html(html);
        var carrusel = complejo.FOTOS_CARRUSEL;
        html = "";
        if (carrusel.length <= 0) {
            html += " <div class='slide slide" + 1 + "' style='background-image: url(img/sin-imagen.png); '>";
            html += "<div class='container'>";
            html += "  <div class='slide-inner" + 1 + " common-inner'>";
            html += " <span class='h1 from-bottom'></span>";
            html += " </div>";
            html += " </div>";
            html += " </div>";
        }
        $.each(carrusel, function (i, obj) {
            html += " <div class='slide slide" + (i + 1) + "' style='background-image: url(" + obj.FOTO + "); '>";
            html += "<div class='container'>";
            html += "  <div class='slide-inner" + (i + 1) + " common-inner'>";
            html += " <span class='h1 from-bottom'></span>";
            html += " </div>";
            html += " </div>";
            html += " </div>";
        });
        $("#carrusel_com").html(html);
        var $heroSlider = $('.main-slider .inner');
        if ($heroSlider.length > 0) {
            $heroSlider.each(function () {
                var loop = $(this).parent().data('loop'), autoplay = $(this).parent().data('autoplay'), interval = $(this).parent().data('interval') || 3000;
                $(this).owlCarousel({items: 1, loop: loop, margin: 0, nav: true, dots: true, navText: [], autoplay: autoplay, autoplayTimeout: interval, autoplayHoverPause: true, smartSpeed: 700, rtl: false});
            });
        }
        var owl = $('.owl-carousel.partnersLogoSlider');
        owl.owlCarousel({loop: true, margin: 28, autoplay: true, autoplayTimeout: 6000, autoplayHoverPause: true, nav: true, dots: false, smartSpeed: 500, rtl: false, responsive: {320: {slideBy: 1, items: 1}, 768: {slideBy: 1, items: 3}, 992: {slideBy: 1, items: 4}}});
        $('a.group').fancybox({'transitionIn': 'elastic', 'transitionOut': 'elastic', 'speedIn': 600, 'speedOut': 200, 'overlayShow': false});
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
            html += "                       <span class='label label-primary'>" + obj.NOMBRE + "</span>";
            html += "                       <a href='category-grid.html'>";
            html += "                           <div class='overlay'>";
            html += "                               <div class='overlayInfo'>";
            html += "                                   <span class='label label-primary'>" + obj.NOMBRE + "</span>";
            html += "                                   <span class='label label-primary'>Reservar</span>";
            html += "                               </div>";
            html += "                           </div>";
            html += "                       </a>";
            html += "                   </figure>";
            html += "                   <div class='figureBody'>";
            html += "                       <h2><i class='fa fa-check-circle' aria-hidden='false'></i> "+obj.NOMBRE_TIPO+"</h2>";
            html += "                       <h5><i class='fa fa-check-circle' aria-hidden='false'></i> Sintetica</h5>";
            html += "                       <h5><i class='fa fa-check-circle' aria-hidden='false'></i> Techada</h5>";
            html += "                   </div>";
            html += "                   <div class='figureFooter'>";
            html += "                       <div class='form-group'>";
            html += "                           <button type='button' class='btn btn-primary' onclick='reservar_cancha(" + obj.ID + ")'>Reservar</button>";
            html += "                       </div>";
            html += "                   </div>";
            html += "               </article>";
            html += "           </div>";
        });
        $("#container").html(html);
        
         var comentarios = complejo.COMENTARIOS;
         var clasificacion=0;
         var html="";
         var totalclasi=0;
         $.each(comentarios,function(i,obj){
               clasificacion=obj.CLASIFICACION;
               totalclasi+=clasificacion;
                html+="<div class='media media-comment'>";
                html+="                        <div class='media-left'>";
                html+="                            <img src='http://graph.facebook.com/" + obj.ID_USR + "/picture?type=large' class='media-object img-circle' alt='Image User'>";
                html+="                        </div>";
                html+="                        <div class='media-body'>";
                html+="                            <h4 class='media-heading'>"+obj.NOMBRE+"</h4>";
                html+="                            <ul class='list-inline rating'>";
                
                for (var i = 0; i < 5; i++) {
                    if(i>=clasificacion){
                          html+="<li><i class='fa fa-star-o' aria-hidden='true'></i></li>";
                    }else{
                          html+="<li><i class='fa fa-star' aria-hidden='true'></i></li>";
                    }
                }
                html+="                            </ul>";
                html+="                            <p>";
                html+=obj.COMENTARIO;
                html+="                           </p>";
                html+="                        </div>";
                html+="                    </div>";
             
         });
          $("#conten-comentario").append(html);
           $("#cant_comentario").html(comentarios.length);
           
           totalclasi=totalclasi/comentarios.length;
           $("#cant_reviu").html(totalclasi+" puntos ( "+comentarios.length+" Reviews)");
           var total=Math.round(totalclasi);
            var html_review="";
                 for (var i = 0; i < 5; i++) {
                    if(i>=total){
                          html_review+="<li><i class='fa fa-star-o' aria-hidden='true'></i></li>";
                    }else{
                          html_review+="<li><i class='fa fa-star' aria-hidden='true'></i></li>";
                    }
                }
                $("#clasificaciones").html(html_review);
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

function reservar_cancha(id) {
    window.location.href = "reserva.html?cmp=" + complejo.ID + "&ic=" + id;
}

function comentar() {
    if (comprovar_session()) {
        var clasificacion = $("input[name=rating]:checked").val();
        var coment = $("#ta_coment").val();
        var id_usr=sessionStorage.getItem("id_log");
        var id_com=complejo.ID;
        $.post(url,{evento:"comentar_complejo",
                    clasi:clasificacion,
                    coment:coment,
                    id_usr:id_usr,
                    id_com:id_com},function(resp){
            if(resp=="falso"){
                alert("Ocurrio un error al efectuar su comentario");
            }else{
                var response = $.parseJSON(sessionStorage.getItem("facebook_data"));
                var html="";
                html+="<div class='media media-comment'>";
                html+="                        <div class='media-left'>";
                html+="                            <img src='http://graph.facebook.com/" + response.id + "/picture?type=large' class='media-object img-circle' alt='Image User'>";
                html+="                        </div>";
                html+="                        <div class='media-body'>";
                html+="                            <h4 class='media-heading'>"+response.name+"</h4>";
                html+="                            <ul class='list-inline rating'>";
                
                for (var i = 0; i < 5; i++) {
                    if(i>=clasificacion){
                          html+="<li><i class='fa fa-star-o' aria-hidden='true'></i></li>";
                    }else{
                          html+="<li><i class='fa fa-star' aria-hidden='true'></i></li>";
                    }
                }
                html+="                            </ul>";
                html+="                            <p>";
                html+=coment;
                html+="                           </p>";
                html+="                        </div>";
                html+="                    </div>";
                $("#conten-comentario").append(html);
                var cant = parseInt($("#cant_comentario").html());
                cant+=1;
                $("#cant_comentario").html(cant);
            }
        });
    }else{
        alert("nesesita inciar sesion para comentar.");
    }

}

function comprovar_session() {
    if (sessionStorage.getItem("id_log")) {
        return true;
    } else {
        return false;
    }
}