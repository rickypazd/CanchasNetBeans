var url = "indexController";
var markers = [];
var map;
var descripcion = 'cancha';
var cant_paginas;
var pag_selec;
function initMap() {
    $.post(url, {evento: "get_complejos"}, function (resp) {
        json = $.parseJSON(resp);
        map = new google.maps.Map(document.getElementById('map'), {
            zoom: 15,
            center: {lat: -17.7848688, lng: -63.180835}

        });
        var cant = json.length;
        $("#cantidad_en_mapa").html(json.length);
        $("#resultados_b").html(json.length);
        $.each(json, function (i, obj) {
            addMarkerWithTimeout(obj, i * 200);
        });
        cargar_complejos(json);
    });
}
function addMarkerWithTimeout(obj, timeout) {
    var b64 = "img/sin-imagen.png";
    if (obj.B64 != "") {
        b64 = obj.B64;
    }
    window.setTimeout(function () {
        var marker = new google.maps.Marker({
            position: {lat: obj.LAT, lng: obj.LNG},
            map: map,
            title: obj.NOMBRE,
            animation: google.maps.Animation.DROP,
            icon: 'img/blue_circle.png'
        });
        var infow = "<div class='container' style='width:400px;'>";
        infow += "<div class='col-md-6'>";
        infow += "<img src='" + b64 + "' alt='can' class='img-responsive' width='100%'/>";
        infow += "</div>";
        infow += "<div class='col-md-6'>";
        infow += "<span style='color:#222222; font-size:16px;'>" + obj.NOMBRE + "</span>";
        infow += "               <div>";
        infow += "               <i class='icon-listy icon-target' style='color: #1bac06; '></i>";
        infow += "               <span class='placeName'>" + obj.DIRECCION + "</span>";
        infow += "                </div>";
       infow += "</div>";
        infow += "</div>";
        infow += "<div>";
          infow += "<span >" + obj.PRESENTACION + "</span>";
          infow += "</div>";
        infow += "<div>";
        infow += "<i class='fa fa-phone' aria-hidden='true'></i>";
        infow += obj.TELEFONOS[0].TELEFONO ;
        infow += "</div>";
        infow += "<div >";
        infow += "<i class='fa fa-envelope' aria-hidden='true'></i><a href='#'></a>";
        infow += "<a href='#'>" + obj.CORREOS[0].CORREO + "</a>";
        infow += "</div>";
            infow += "<button type='button' onclick='ver_mas(" + obj.ID + ");' style='background-color: #1bac06; color: white; padding: 5px; border-radius:.25em;'>Ver mas </button>";
       
        var infowindow = new google.maps.InfoWindow({
            content: infow
        });
        markers.push(marker);
        google.maps.event.addListener(marker, 'click', function () {
            infowindow.open(map, marker);
        });

    }, timeout);
}

function cargar_complejos(json) {
    var html = "";
    var cant = json.length;
    var npag = Math.ceil(cant / 4);
    cant_paginas = npag;
    var cont = 0;
    var cont_pag = 0;
    $.each(json, function (i, obj) {
        var b64 = "img/sin-imagen.png";
        if (obj.B64 != "") {
            b64 = obj.B64;
        }
        if (cont == 0) {
            cont_pag++;
            html += "<div id='pagina_codigo" + cont_pag + "'>";
        }
        html += "<div class='listContent'>";
        html += "    <div class='row'>";
        html += "       <div class='col-sm-5 col-xs-12'>";
        html += "           <div class='categoryImage'>";
        html += "               <img src='" + b64 + "' alt='Image category' class='img-responsive img-rounded'>";
        html += "               <span class='label label-primary'>" + obj.NOMBRE + "</span>";
        html += "       </div>";
        html += "    </div>";
        html += "    <div class='col-sm-7 col-xs-12'>";
        html += "       <div class='categoryDetails'>";
        html += "            <h2>";
        html += "                <a href='detallecanchas.html?id=" + obj.ID + "' style='color:#222222'>" + obj.NOMBRE + "</a>";
        html += "               <span class='likeCount'>";
        html += "                <i class='fa fa-heart-o' aria-hidden='true'></i> 10 k";
        html += "               </span>";
        html += "           </h2>";
        html += "           <h5>Santa cruz de la sierra</h5>";
        html += "               <p>";
        html += "               <i class='icon-listy icon-target' style='color: #1bac06; '></i>";
        html += "               <span class='placeName'>" + obj.DIRECCION + "</span>";
        html += "                </p>";
        html += "               <p style='text-align:justify'>" + obj.PRESENTACION + "</p>";
        html += "               <ul class='list-inline list-tag'>";
        html += "                    <li><i class='fa fa-phone' aria-hidden='true'></i></li>";
        html += "                   <li>" + obj.TELEFONOS[0].TELEFONO + "</li>";
        html += "                </ul>";
        html += "               <ul class='list-inline list-tag'>";
        html += "                   <li><i class='fa fa-envelope' aria-hidden='true'></i><a href='#'></a></li>";
        html += "                   <li><a href='#'>" + obj.CORREOS[0].CORREO + "</a></li>";
        html += "               </ul>";
        html += "               <ul class='list-inline list-tag'>";
        html += "                    <li><button type='button' onclick='ver_mas(" + obj.ID + ");' style='background-color: #1bac06; color: white; padding: 5px; border-radius:.25em; line-height: 23px;'>Ver mas </button></li>";
        html += "                </ul>";
        html += "            </div>";
        html += "       </div>";
        html += "    </div>";
        html += "</div>";
        cont++;
        if (cont == 4) {
            cont = 0;
            html += "</div>";
        } else if (i + 1 == cant) {
            html += "</div>";
        }
    });
    $("#conten_paginas_comp").html(html);
    $("[id*=pagina_codigo]").css("display", "none");
    $("#pagina_codigo1").css("display", "");
    pag_selec = 1;
    var htmlpa = "";
    var con = 1;
    htmlpa += "<li>";
    htmlpa += "   <a href='javascript:void(0)' onclick='anterior();' aria-label='Previous'>";
    htmlpa += "      <span aria-hidden='true'><i class='fa fa-angle-left' aria-hidden='true'></i></span>";
    htmlpa += "   </a>";
    htmlpa += "</li>";
    while (con <= npag) {
        if (con == 1) {
            htmlpa += "<li class='active'><a href='javascript:void(0)' id='btn_pag" + con + "' onclick='ver_pagina(" + con + ")'>" + con + "</a></li>";
        } else {
            htmlpa += "<li ><a href='javascript:void(0)' id='btn_pag" + con + "' onclick='ver_pagina(" + con + ")'>" + con + "</a></li>";
        }
        con++;
    }

    htmlpa += "<li>";
    htmlpa += "   <a href='javascript:void(0)' onclick='siguiente();' aria-label='Next'>";
    htmlpa += "       <span aria-hidden='true'><i class='fa fa-angle-right' aria-hidden='true'></i></span>";
    htmlpa += "   </a>";
    htmlpa += "</li>";
    $("#paginat_cont").html(htmlpa);
}

function ver_mas(id) {
    window.location.href = "detallecanchas.html?id=" + id;
}


function ver_pagina(pag) {

    $("[id*=pagina_codigo]").css("display", "none");
    $("#pagina_codigo" + pag).css("display", "");
    $("#paginat_cont").find("li.active").attr("class", "");
    $("#btn_pag" + pag).parent("li").attr("class", "active");
    $("#conten_paginas_comp").focus();
    pag_selec = pag;
    $('body, html, #conten_paginas_comp').scrollTop(0);
}

function anterior() {
    var pag = pag_selec - 1;
    if (pag > 0) {
        ver_pagina(pag);
    }
}
function siguiente() {
    var pag = pag_selec + 1;
    if (pag <= cant_paginas) {
        ver_pagina(pag);
    }
}