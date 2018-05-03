/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var id_cancha;
var url = "adminController";
$(document).ready(function () {
    rezizar();
    $("#progressbar").css("display", "none");
    $("#cargando_foto").css("display", "none");
    id_cancha = sessionStorage.getItem("id_cancha");
    cargar_cancha();
});
function rezizar() {
    var h = $(document).height();
    window.parent.resizes(h + 15);
}

function cargar_cancha() {
    $.post(url, {evento: "get_complejos_id", id: id_cancha}, function (resp) {

        var cancha = $.parseJSON(resp);
        var url_img = "img/sin-imagen.png";
        if (cancha.FOTO_PERFIL != "") {
            url_img = cancha.FOTO_PERFIL + "";
        }
        $("#complejo_perfil").attr("src", "../" + url_img);
        $("#nombre").attr("placeholder", cancha.NOMBRE);
        $("#direccion").attr("placeholder", cancha.DIRECCION);
        $("#presentacion").attr("placeholder", cancha.PRESENTACION);
        $("#politicas").attr("placeholder", cancha.POLITICAS);
        var telefonos = cancha.TELEFONOS;
        var html = "";
        $.each(telefonos, function (i, obj) {
            html += "<input type='text' class='form-control' name='" + obj.ID + "' placeholder='" + obj.TELEFONO + "'/>";
        });
        $("#telefonos").html(html);
        var correos = cancha.CORREOS;
        html = "";
        $.each(correos, function (i, obj) {
            html += "<input type='text' class='form-control' name='" + obj.ID + "' placeholder='" + obj.CORREO + "'/>";
        });
        $("#emails").html(html);
        var carrusel = cancha.FOTOS_CARRUSEL;
        html = "";
        $.each(carrusel, function (i, obj) {
            html += "  <img src='../" + obj.FOTO + "' alt='Image User' class='img-circle img-carr-b' width='100' height='100' onclick='eliminar_foto_carr(" + obj.ID + ",this);' />";
        });
        $("#fotos_carrusel").html(html);
        var canchas = cancha.CANCHAS;
        html = "";
        $.each(canchas, function (i, obj) {
            html += "<div class='col-md-4 col-sm-6'>";
                html += "<div class='cancha_conten'>";
                    html += "<img src='../img/canchas/cancha1.jpg' class='img-responsive'>";
                    html += "<div>";
                    html += obj.NOMBRE;
                    html += "</div>  ";
                    html += "<div>";
                        html += "<h2><i class='fa fa-check-circle' aria-hidden='false'></i> Futbol 7</h2>";
                    html += "</div>";
                    html +="<button type='submit' class='btn btn-primary'> editar </button>";
                html += "</div>";
            html += "</div>";

        });
        $("#cont_canchas").html(html);
        rezizar();
    });
}

function guardar_cambio() {
    var nombre = $("#nombre").val() || 0;
    var direccion = $("#direccion").val() || 0;
    var presentacion = $("#presentacion").val() || 0;
    if (presentacion != 0) {
        presentacion = presentacion.replace(/\n/g, "<br />");
    }
    var politicas = $("#politicas").val() || 0;
    if (politicas != 0) {
        politicas = politicas.replace(/\n/g, "<br />");
    }
    var json_tel = '{"telefonos":[';
    var input_telefonos = $("#telefonos").children("input");
    var tel, id;
    $.each(input_telefonos, function (i, obj) {
        tel = $(obj).val() || 0;
        id = $(obj).attr("name") || 0;
        if (tel != 0) {
            json_tel += '{"tel":"' + tel + '","id":' + id + '},';
        }
    });
    json_tel = json_tel.substring(0, json_tel.length - 1);
    json_tel += ']}';
    var json = '{"correos":[';
    var input_correos = $("#emails").children("input");
    var correo;
    $.each(input_correos, function (i, obj) {
        correo = $(obj).val() || 0;
        id = $(obj).attr("name") || 0;
        if (correo != 0) {
            json += '{"corr":"' + correo + '","id":' + id + '},';
        }
    });
    json = json.substring(0, json.length - 1);
    json += ']}';
    $.post(url, {evento: "editar_cancha",
        tele: json_tel,
        corre: json,
        presentacion: presentacion,
        politicas: politicas,
        id_cancha: id_cancha,
        nombre: nombre,
        direccion: direccion}, function (resp) {
        if (resp == "EXITO") {
            alert("Complejo creado, Desde el panel de administracion de complejos podra agregar fotos y editar datos del mismo.");
            change_frame_padre("inicio");
        }
    });
}

function subir_foto_perfil() {
    $("input[name=foto_perfil]").click();
}
function subir_foto_carrusel() {
    $("input[name=foto_carr]").click();
}

function subir_foto() {
    $("input[name=id_complejo]").val(id_cancha);
    var formData = new FormData($("#formimg_perfil")[0]);
    $("#progressbar").css("display", "");
    $("#cargando_foto").css("display", "");
    var progressbar = $("#progressbar"),
            progressLabel = $(".progress-label");
    progressbar.progressbar({
        value: false,
        change: function () {
            progressLabel.text(progressbar.progressbar("value") + "%");
        },
        complete: function () {
            progressLabel.text("Complete!");
        }
    });
    $.ajax({
        url: url,
        type: 'POST',
        data: formData,
        mimeType: "multipart/form-data",
        contentType: false,
        cache: false,
        processData: false,
        xhr: function () {
            var myXhr = $.ajaxSettings.xhr();
            if (myXhr.upload) {
                // For handling the progress of the upload
                myXhr.upload.addEventListener('progress', function (e) {
                    if (e.lengthComputable) {
                        progressbar.progressbar("value", parseInt((e.loaded * 100) / e.total));
                    }
                }, false);
            }
            return myXhr;
        },
        success: function (data, textStatus, jqXHR)
        {
            ///despues de cargar
            $("input[name=foto_perfil]").val("");
            $("#progressbar").css("display", "none");
            $("#cargando_foto").css("display", "none");
            $("#complejo_perfil").attr('src', "../" + data);
        }
    });
}

function subir_foto_carrusel_s() {
    $("input[name=id_complejo_c]").val(id_cancha);
    var formData = new FormData($("#formimg_carrusel")[0]);
    $("#progressbar").css("display", "");
    $("#cargando_foto").css("display", "");
    var progressbar = $("#progressbar"),
            progressLabel = $(".progress-label");
    progressbar.progressbar({
        value: false,
        change: function () {
            progressLabel.text(progressbar.progressbar("value") + "%");
        },
        complete: function () {
            progressLabel.text("Complete!");
        }
    });
    $.ajax({
        url: url,
        type: 'POST',
        data: formData,
        mimeType: "multipart/form-data",
        contentType: false,
        cache: false,
        processData: false,
        xhr: function () {
            var myXhr = $.ajaxSettings.xhr();
            if (myXhr.upload) {
                // For handling the progress of the upload
                myXhr.upload.addEventListener('progress', function (e) {
                    if (e.lengthComputable) {
                        progressbar.progressbar("value", parseInt((e.loaded * 100) / e.total));
                    }
                }, false);
            }
            return myXhr;
        },
        success: function (data, textStatus, jqXHR)
        {
            ///despues de cargar
            $("input[name=foto_carr]").val("");
            $("#progressbar").css("display", "none");
            $("#cargando_foto").css("display", "none");
            var obj = $.parseJSON(data);
            var html = "  <img src='../" + obj.FOTO + "' alt='Image User' class='img-circle img-carr-b' width='100' height='100' onclick='eliminar_foto_carr(" + obj.ID + ",this);' />";
            $("#fotos_carrusel").append(html);
        }
    });
}

function eliminar_foto_carr(id, foto) {
    if (window.confirm("Desea eliminar la foto?")) {
        $.post(url, {evento: "eliminar_foto_carrusel", id: id}, function (resp) {
            if (resp == "exito") {
                $(foto).css("display", "none");
            } else {
                alert("Ocurrio algun error intentelo nuevamente");
            }

        });
    }
}
function crear_cancha() {
    change_frame_padre("agg_cancha");
}
function change_frame_padre(dir) {
    window.parent.change_frame(dir);
}