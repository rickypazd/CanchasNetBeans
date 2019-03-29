var url = "indexController";
var markers = [];
var map;
var descripcion = 'cancha';
var cant_paginas;
var pag_selec;
function initMap() {
    map = new google.maps.Map(document.getElementById('map'), {
        zoom: 13,
        center: {lat: -17.7797787, lng: -63.1882631}

    });

    var resp = sessionStorage.getItem("complejoslat");
    json = $.parseJSON(resp);
    if (json) {
        cargar_complejos(json);
        $.each(json, function (i, obj) {
            addMarkerWithTimeout(obj, i * 100);

        });
    }
    $.post(url, {evento: "get_complejos_opti"}, function (resp) {
        sessionStorage.setItem("complejoslat", resp);
        json = $.parseJSON(resp);
        for (var i = 0; i < markers.length; i++) {
            markers[i].setMap(null);
            markers = new Array();

        }
        $("#lista_complejos").html("");
        cargar_complejos(json);
        $.each(json, function (i, obj) {
            addMarkerWithTimeout(obj, i * 100);

        });
        //cargar_complejos(json);
    });


}
function addMarkerWithTimeout(obj, timeout) {
    var b64 = "img/sin-imagen.png";
    if (obj.FOTO_PERFIL != "") {
        b64 = obj.FOTO_PERFIL || "";
    }
    window.setTimeout(function () {
        var marker = new google.maps.Marker({
            position: {lat: obj.LAT, lng: obj.LNG},
            map: map,
            title: obj.NOMBRE,
            animation: google.maps.Animation.DROP,
            icon: 'img/marker-verde.png'
        });

//       var infow = "<div class=' container complejo_iten complejo_map' onclick='ver_complejo(" + JSON.stringify(obj) + ");> ";
//        infow += "   <div style='width: 48%; height: 280px; display: inline-block; background-color: #ffffff88; '>";
//        infow += "                <img src='" + obj.FOTO_PERFIL + "' alt='img'/>";
//        infow += "  </div>";
//        infow += "                <div class='complejo_div' >";
//        infow += "                    <div class='complejo-cont'>";
//        infow += "                        <h3>" + obj.NOMBRE + "</h3>";
//        infow += "                        <p> <i class='fa fa-map-marker icon-dash' aria-hidden='true'></i>" + obj.DIRECCION + "</p>";
//        infow += "                        <p> <i class='fa fa-mobile icon-dash' aria-hidden='true'></i>" + obj.TELEFONO + "</p>";
//        infow += "                        <p> <i class='fa fa-envelope icon-dash' aria-hidden='true'></i>" + obj.CORREO + "</p>";
//        infow += "                    </div>";
//        infow += "                </div>";
//        
//       
//
//        var infowindow = new google.maps.InfoWindow({
//            content: infow
//        });
        markers.push(marker);
        google.maps.event.addListener(marker, 'click', function () {
            infowindow.open(map, marker);
        });


    }, timeout);
}

function cargar_complejos(arr) {
    $.each(arr, function (i, obj) {
        complejo_iten(obj);
    });
}

function complejo_iten(obj) {
    var html = "<div class='col-md-12 complejo_iten com_iten_map' onclick='ver_complejo(" + JSON.stringify(obj) + ");'>";
    html += "   <div style='width: 48%; height: 180px; display: inline-block; background-color: #00000033; '>";
    html += "                <img src='" + obj.FOTO_PERFIL + "' alt='img'/>";
    html += "  </div>";
    html += "                <div class='complejo_div comple_mapa' >";
    html += "                    <div class='complejo-cont'>";
    html += "                        <h3>" + obj.NOMBRE + "</h3>";
    html += "                        <p> <i class='fa fa-map-marker icon-dash' aria-hidden='true'></i>" + obj.DIRECCION + "</p>";
    html += "                        <p> <i class='fa fa-mobile icon-dash' aria-hidden='true'></i>" + obj.TELEFONO + "</p>";
    html += "                        <p> <i class='fa fa-envelope icon-dash' aria-hidden='true'></i>" + obj.CORREO + "</p>";
    html += "                    </div>";
    html += "                </div>";
    html += "            </div>";
    $("#lista_complejos").append(html);
}