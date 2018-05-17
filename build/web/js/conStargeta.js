/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var url = "indexController";
var id_cancha;
var id_log;
var json_res;
$(document).ready(function () {
    id_cancha=getQueryVariable("ic");
    id_log=sessionStorage.getItem("id_log");
    json_res=$.parseJSON(sessionStorage.getItem("json_reserva"));
    cargar_tabla();
});

function cargar_tabla(){
    var total=0;
    var html="";
    $.each(json_res,function(i,obj){
        html+="<tr>";
        html+="<td>"+obj.fecha+"</td>";
        html+="<td>"+obj.hora+"</td>";
        html+="<td>"+obj.costo+"</td>";
        html+="</tr>";
        total+=obj.costo;
    });
    
    $("#t_res").html(html   );
    $("#td_total").html(total);
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
function ok_reservar(){
    var strin=JSON.stringify(json_res);
    $.post(url,{evento:"ok_res_sin_targeta",id_can:id_cancha,id_usr:id_log,json:strin},function(resp){
        alert("exito");
    });
}