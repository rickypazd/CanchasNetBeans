/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var url = "adminController";
var usr_log;
$(document).ready(function () {
     $(document).scroll(evt_scroll);
    if (sessionStorage.getItem("usr_log")) {
        usr_log = $.parseJSON(sessionStorage.getItem("usr_log"));
    }
    cargar_reservas();
});

function cargar_reservas() {
    $.post(url, {evento: "get_reservas_admin", id: usr_log.ID}, function (resp) {
        var json = $.parseJSON(resp);
        var html = "";
        $.each(json, function (i, obj) {
            html += "<tr>";
            html += "<td>" + obj.ID_RES_PA + "</td>";
            html += "<td>" + obj.NOMBRE_COMP + "</td>";
            html += "<td>" + obj.NOMBRE_CAN + "</td>";
            html += "<td>" + get_tipo_pago(obj.TIPO_PAGO) + "</td>";
            html += "<td>" + obj.TOTAL + "</td>";
            html += "<td>" + obj.HORAS + "</td>";
            html += "<td>" + obj.FECHA + "</td>";
            html += "<td>" + get_estado(obj.ESTADO) + "</td>";
            html += "<td><button type='button' onclick='ver_detalle_res("+obj.ID_RES_PA+")'>VER DETALLE</button></td>";
            html += "</tr>";
        });
        $("#table_res").html(html);
        rezizar();
    });
}
function get_tipo_pago(tipo) {
    var resp = "";
    switch (tipo) {
        case 1:
            resp = "EFECTIVO";
            break;
        case 2:
            resp = "TARGETA";
            break;
    }
    return resp;
}
function get_estado(estado) {
    var resp = "";
    switch (estado) {
        case 1:
            resp = "PENDIENTE";
            break;
        case 2:
            resp = "CONFIRMADO";
            break;
        case 3:
            resp = "CANCELADO";
            break;
    }
    return resp;
}

function ver_detalle_res(id_res){
    sessionStorage.setItem("id_res_detalle",id_res);
    change_frame_padre("reserva_detalle");
    
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