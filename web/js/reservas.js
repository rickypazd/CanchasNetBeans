/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var url="indexController";
var id_log;
$(document).ready(function(){
     id_log=sessionStorage.getItem("id_log");
    cargar_reservas();
});

function cargar_reservas() {
    $.post(url, {evento: "get_mis_reservas", id: id_log}, function (resp) {
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
            html += "<td>" + obj.FECHA.substring(0,16); + "</td>";
            html += "<td>" + get_estado(obj.ESTADO) + "</td>";
            html += "<td><div class='btn-group'><button type='button' class='btn btn-primary'>Cancelar</button></div></td>";
            html += "</tr>";
        });
        $("#table_res_usr").html(html);
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
            resp = "<span class='label label-warning'>Pendiente</span>";
            break;
        case 2:
            resp = "<span class='label label-success'>Confirmada</span>";
            break;
        case 3:
            resp = "<span class='label label-danger'>Cancelada</span>";
            break;
    }
    return resp;
}

