/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var latlon;
var url = "adminController";

$(document).ready(function () {

    $('.timepicker').timepicker({
        showInputs: false
    });
    $("#registro_2").css("display", "none");
    $(".horariodiv").css("display", "none");
    CKEDITOR.replace('presentacion', {
        toolbarGroups: [

            {name: 'basicstyles', groups: ['basicstyles', 'cleanup']},
            {name: 'paragraph', groups: ['list', 'indent', 'blocks', 'align', 'bidi']},
            {name: 'styles'},
            {name: 'colors'}
        ]
    });
    CKEDITOR.replace('politicas', {
        toolbarGroups: [

            {name: 'basicstyles', groups: ['basicstyles', 'cleanup']},
            {name: 'paragraph', groups: ['list', 'indent', 'blocks', 'align', 'bidi']},
            {name: 'styles'},
            {name: 'colors'}
        ]
    });
    initMap();
});
var nombre;
var presentacion;
var direccion;
var politicas;
function siguiente() {
    nombre = $("#nombre").val() || 0;
    presentacion = CKEDITOR.instances['presentaciona'].getData() || 0;
    politicas = CKEDITOR.instances['politicasa'].getData() || 0;
    direccion = $("#direccion").val() || 0;

    $("#nombre").css("background", "#ffffff");
    $("#presentaciona").css("background", "#ffffff");
    $("#direccion").css("background", "#ffffff");
    $("#politicasa").css("background", "#ffffff");

    if (nombre <= 0) {
        $("#nombre").css("background", "#F09C84");
        $("#nombre").focus();
        return;
    }
    if (direccion <= 0) {
        $("#direccion").css("background", "#F09C84");
        $("#direccion").focus();
        return;
    }
    if (presentacion <= 0) {
        $("#presentaciona").css("background", "#F09C84");
        $("#presentaciona").focus();
        return;
    }

    if (politicas <= 0) {
        $("#politicasa").css("background", "#F09C84");
        $("#politicasa").focus();
        return;
    }
    
    if (latlon == null) {
        alert("Deve seleccionar una posicion en el mapa");
        return;
    }
    
    $("#registro_1").css("display", "none");
    $("#registro_2").css("display", "");


}
function atras() {
    $("#registro_1").css("display", "");
    $("#registro_2").css("display", "none");
}
function add_telefono() {
    $("#div_telefonos").append("<input type='text' class='form-control' placeholder='telefono'>");
}
function add_email() {
    $("#div_email").append("<input type='email' class='form-control' placeholder='email'>");
}
function registrar() {
    var json = '{"telefonos":[';
    var input_telefonos = $("#div_telefonos").children("input");
    var tel;
    $.each(input_telefonos, function (i, obj) {
        tel = $(obj).val() || 0;
        if (i == 0) {
            if (tel <= 0) {
                $(obj).css("background", "#F09C84");
                $(obj).focus();
                return;
            } else {
                $(obj).css("background", "#ffffff");
            }
        }
        if (tel <= 0) {
        } else {
            json += '{"tel":"' + tel + '"},';
        }
    });
    json = json.substring(0, json.length - 1);
    json += ']';
    json += ',"correos":[';
    var input_correos = $("#div_email").children("input");
    var correo;
    $.each(input_correos, function (i, obj) {
        correo = $(obj).val() || 0;
        if (i == 0) {
            if (correo <= 0) {
                $(obj).css("background", "#F09C84");
                $(obj).focus();
                return;
            } else {
                $(obj).css("background", "#ffffff");
            }
        }
        if (correo <= 0) {
        } else {
            json += '{"corr":"' + correo + '"},';
        }
    });
        json = json.substring(0, json.length - 1);
    json += '],';
    json += '"horario":[';
    var hora_ini;
    var hora_fin;
    if ($("#clunes").is(":checked")) {
        json += '{"act":"si",';
        hora_ini = $("#clunes").parent().find(".hora_ini").val();
        hora_fin = $("#clunes").parent().find(".hora_fin").val();
        json += '"hora_ini":"' + hora_ini + '",';
        json += '"hora_fin":"' + hora_fin + '"},';
    } else {
        json += '{"act":"no",';
        hora_ini = "00:00 PM";
        hora_fin = "00:00 PM";
        json += '"hora_ini":"' + hora_ini + '",';
        json += '"hora_fin":"' + hora_fin + '"},';
    }
    if ($("#cmartes").is(":checked")) {
        json += '{"act":"si",';
        hora_ini = $("#cmartes").parent().find(".hora_ini").val();
        hora_fin = $("#cmartes").parent().find(".hora_fin").val();
        json += '"hora_ini":"' + hora_ini + '",';
        json += '"hora_fin":"' + hora_fin + '"},';
    } else {
        json += '{"act":"no",';
        hora_ini = "00:00 PM";
        hora_fin = "00:00 PM";
        json += '"hora_ini":"' + hora_ini + '",';
        json += '"hora_fin":"' + hora_fin + '"},';
    }
    if ($("#cmiercoles").is(":checked")) {
        json += '{"act":"si",';
        hora_ini = $("#cmiercoles").parent().find(".hora_ini").val();
        hora_fin = $("#cmiercoles").parent().find(".hora_fin").val();
        json += '"hora_ini":"' + hora_ini + '",';
        json += '"hora_fin":"' + hora_fin + '"},';
    } else {
        json += '{"act":"no",';
        hora_ini = "00:00 PM";
        hora_fin = "00:00 PM";
        json += '"hora_ini":"' + hora_ini + '",';
        json += '"hora_fin":"' + hora_fin + '"},';
    }
    if ($("#cjueves").is(":checked")) {
        json += '{"act":"si",';
        hora_ini = $("#cjueves").parent().find(".hora_ini").val();
        hora_fin = $("#cjueves").parent().find(".hora_fin").val();
        json += '"hora_ini":"' + hora_ini + '",';
        json += '"hora_fin":"' + hora_fin + '"},';
    } else {
        json += '{"act":"no",';
        hora_ini = "00:00 PM";
        hora_fin = "00:00 PM";
        json += '"hora_ini":"' + hora_ini + '",';
        json += '"hora_fin":"' + hora_fin + '"},';
    }
    if ($("#cviernes").is(":checked")) {
        json += '{"act":"si",';
        hora_ini = $("#cviernes").parent().find(".hora_ini").val();
        hora_fin = $("#cviernes").parent().find(".hora_fin").val();
        json += '"hora_ini":"' + hora_ini + '",';
        json += '"hora_fin":"' + hora_fin + '"},';
    } else {
        json += '{"act":"no",';
        hora_ini = "00:00 PM";
        hora_fin = "00:00 PM";
        json += '"hora_ini":"' + hora_ini + '",';
        json += '"hora_fin":"' + hora_fin + '"},';
    }
    if ($("#csabado").is(":checked")) {
        json += '{"act":"si",';
        hora_ini = $("#csabado").parent().find(".hora_ini").val();
        hora_fin = $("#csabado").parent().find(".hora_fin").val();
        json += '"hora_ini":"' + hora_ini + '",';
        json += '"hora_fin":"' + hora_fin + '"},';
    } else {
        json += '{"act":"no",';
        hora_ini = "00:00 PM";
        hora_fin = "00:00 PM";
        json += '"hora_ini":"' + hora_ini + '",';
        json += '"hora_fin":"' + hora_fin + '"},';
    }
    if ($("#cdomingo").is(":checked")) {
        json += '{"act":"si",';
        hora_ini = $("#cdomingo").parent().find(".hora_ini").val();
        hora_fin = $("#cdomingo").parent().find(".hora_fin").val();
        json += '"hora_ini":"' + hora_ini + '",';
        json += '"hora_fin":"' + hora_fin + '"}],';
    } else {
        json += '{"act":"no",';
        hora_ini = "00:00 PM";
        hora_fin = "00:00 PM";
        json += '"hora_ini":"' + hora_ini + '",';
        json += '"hora_fin":"' + hora_fin + '"}],';
    }

    json += '"lat":' + latlon.lat() + ',';
    json += '"lng":' + latlon.lng() + '}';
    var usr_loge = $.parseJSON(sessionStorage.getItem("usr_log"));

    $.post(url, {evento: "crear_cancha",
        datos: json,
        presentacion: presentacion,
        politicas: politicas,
        id_usr: usr_loge.ID,
        nombre: nombre,
        direccion: direccion}, function (resp) {
       
            sessionStorage.setItem("id_cancha",resp);
            alert("Complejo creado, Desde el panel de administracion de complejos podra agregar fotos y editar datos del mismo.");
            
            change_frame_padre("ver_cancha_admin");
        
    });
}

function abilitar(inpu) {
    if ($(inpu).is(":checked")) {
        $(inpu).parent().children(".horariodiv").css("display", "");
    } else {
        $(inpu).parent().children(".horariodiv").css("display", "none");
    }
}
var marker;
function initMap() {
    var uluru = {lat: -17.78595866469323, lng: -63.17859649658203};
    var map = new google.maps.Map(document.getElementById('map'), {
        zoom: 13,
        center: uluru
    });
   

    map.addListener('click', function (event) {
        latlon = event.latLng;
        if (marker == null) {
            marker = new google.maps.Marker({
                position: uluru,
                map: map
            });
        }
        marker.setPosition(latlon);

    });
}
function change_frame_padre(dir) {
    window.parent.change_frame(dir);
}