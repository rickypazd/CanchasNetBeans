/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


var url = "adminController";
$(document).ready(function () {
    cargar_tipos();
});

function add_tipo() {
    var tipo = $("#input_tipo").val() || 0;
    $("#input_tipo").css("background", "#ffffff");

    if (tipo <= 0) {
        $("#input_tipo").css("background", "#F09C84");
        $("#input_tipo").focus();
        return;
    }
    $.post(url, {evento: "agg_tipo_cancha", tipo: tipo}, function (resp) {
        var html = "<li>" + resp + "</li>";
        $("#lista_tipo").append(html);

    });
}

function cargar_tipos() {
    $.post(url, {evento: "get_tipos_canchas"}, function (resp) {
        var json = $.parseJSON(resp);
        var html = "";
        $.each(json, function (i, obj) {
            html += "<li>" + obj.TIPO + "</li>";
        });
        $("#lista_tipo").html(html);

    });
}