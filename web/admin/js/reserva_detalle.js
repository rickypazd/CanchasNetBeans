/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var urlad="adminController";
var url="../indexController";   
var json_detalle;
var id_comp;
var id_cancha;
var domingo_selec;
var json_horario;
$(document).ready(function(){
    var id_res=sessionStorage.getItem("id_res_detalle");
      domingo_selec = Date.today().moveToDayOfWeek(0, -1);
    $.post(urlad,{evento:"get_detalle_res",id:id_res},function(resp){
        json_detalle=$.parseJSON(resp);
        $("#id_com").html(json_detalle.ID_COM);
        $("#id_ca").html(json_detalle.ID_CA);
        $("#nombre_com").html(json_detalle.NOMBRE_COM);
        $("#nombre_ca").html(json_detalle.NOMBRE_CA);
        $("#nombre_usr").html(json_detalle.NOMBRE_USR);
        id_comp=json_detalle.ID_COM;
        id_cancha=json_detalle.ID_CA;
        
        cargar_tabla_costos(id_comp);
    });
});


function cargar_tabla_costos(id) {
    $.post(url, {evento: "get_horario_complejo", id: id}, function (resp) {
        var json = $.parseJSON(resp);
        json_horario = json;
        var cont = 0;
        var html = "<tr>";
        html += "<th>HORA</th>";
        var hora_noche = Date.parseExact("12:00 AM", "hh:mm tt");
        var hora_final_dia = Date.parseExact("11:59 PM", "hh:mm tt");
        var hora_ini;
        var hora_fin;
        var menor_hora_fin;
        var menor_hora_ini;
        var menor_hora_ini_copi;
        var hora_formateada;
        var tim = 1000000000;
        var t = 1000000000;



        $.each(json, function (i, obj) {

            if (obj.ABIERTO == "1") {
                hora_ini = Date.parseExact(obj.HORA_INI, "hh:mm tt");
                var ha = hora_ini.getTime() - hora_noche.getTime();
                if (ha < tim) {
                    tim = ha;
                    menor_hora_ini = hora_ini;
                }
                hora_fin = Date.parseExact(obj.HORA_FIN, "hh:mm tt");
                var h = hora_final_dia.getTime() - hora_fin.getTime();
                if (h < t) {
                    t = h;
                    menor_hora_fin = hora_fin;
                }

                html += "<th class='" + obj.DIA + "'>" + get_dia(obj.DIA) + "<span></span></th>";
                cont++;
            }
        });
        html += "</tr>";
        menor_hora_ini_copi = menor_hora_ini;
        var horas = menor_hora_fin.getTime() - menor_hora_ini.getTime();
        horas = ((horas / 1000) / 60) / 60;
        html += "<tr>";
        html += "<td>" + formatAMPM(menor_hora_ini) + "</td>";
        var aux = cont;
        var hora_ini_aux;
        var hora_fin_aux;
        hora_formateada = time_format(menor_hora_ini);
        $.each(json, function (i, obj) {
            if (obj.ABIERTO == "1") {
                hora_ini_aux = Date.parseExact(obj.HORA_INI, "hh:mm tt");
                hora_fin_aux = Date.parseExact(obj.HORA_FIN, "hh:mm tt");
                if (hora_ini_aux.getHours() <= menor_hora_ini.getHours() && hora_fin_aux.getHours() >= menor_hora_ini.getHours()) {
                    html += "<td  data-hora='" + hora_formateada + "' data-dia='" + obj.DIA + "' class='no_selec' onclick='marcar_reserva(this);'></td>";
                } else {
                    html += "<td style='background:#3f3f3f'></td>";
                }
            }
        });
        html += "</tr>";

        while (horas > 1) {
            aux = cont
            menor_hora_ini.setHours(menor_hora_ini.getHours() + 1);
            hora_formateada = time_format(menor_hora_ini);
            html += "<tr>";
            html += "<td>" + formatAMPM(menor_hora_ini) + "</td>";

            $.each(json, function (i, obj) {
                if (obj.ABIERTO == "1") {
                    hora_ini_aux = Date.parseExact(obj.HORA_INI, "hh:mm tt");
                    hora_fin_aux = Date.parseExact(obj.HORA_FIN, "hh:mm tt");
                    if (hora_ini_aux.getHours() <= menor_hora_ini.getHours() && hora_fin_aux.getHours() >= menor_hora_ini.getHours()) {
                        html += "<td class='no_selec' data-hora='" + hora_formateada + "' data-dia='" + obj.DIA + "' onclick='marcar_reserva(this);'></td>";
                        ;
                    } else {
                        html += "<td style='background:#3f3f3f'></td>";
                    }

                }
            });
            html += "</tr>";
            horas--;
        }
        aux = cont;
        html += "<tr>";
        html += "<td>" + formatAMPM(menor_hora_fin) + "</td>";
        var aux = cont;
        hora_formateada = time_format(menor_hora_fin);
        $.each(json, function (i, obj) {
            if (obj.ABIERTO == "1") {
                hora_ini_aux = Date.parseExact(obj.HORA_INI, "hh:mm tt");
                hora_fin_aux = Date.parseExact(obj.HORA_FIN, "hh:mm tt");
                if (hora_ini_aux.getHours() <= menor_hora_fin.getHours() && hora_fin_aux.getHours() >= menor_hora_fin.getHours()) {
                    html += "<td class='no_selec' data-hora='" + hora_formateada + "' data-dia='" + obj.DIA + "' onclick='marcar_reserva(this);'></td>";
                } else {
                    html += "<td style='background:#3f3f3f'></td>";
                }
            }
        });
        html += "</tr>";
        horas--;
        $("#ordersTable").html(html);

        cargar_precios();
    });
}
function cargar_precios() {
    $.post(url, {evento: "get_precio_cancha", id: id_cancha}, function (resp) {
        var json_cos = $.parseJSON(resp);
        var hora_p;
        $.each(json_cos, function (i, obj) {
            hora_p = obj.HORA;
            hora_p = hora_p.substring(0, 5);

            $("#ordersTable").find("td[data-dia='" + obj.DIA + "'][data-hora='" + hora_p + "']").html(obj.PRECIO);

        });
        cargar_fecha();
    });

}
function cargar_fecha() {
    var fecha_dia_string;
    $.each(json_horario, function (i, obj) {
        if (obj.ABIERTO == "1") {
            fecha_dia_string = get_fecha_dia(obj.DIA, domingo_selec.clone()).toString("dd/MM/yy");
            var thdia = $("#ordersTable").find("th." + obj.DIA);
            $(thdia).find("span").html(" " + fecha_dia_string);
            var dias = $("#ordersTable").find("td[data-dia='" + obj.DIA + "']");
            $.each(dias, function (i, obj) {
                $(obj).data("fecha", fecha_dia_string);
                $(obj).attr("onclick", "marcar_reserva(this)");
                $(obj).attr("class", "no_selec");

            });
        }
    });
    var fecha_ini = get_fecha_dia("0", domingo_selec.clone()).toString("yyyy-MM-dd");
    var fecha_fin = get_fecha_dia("0", domingo_selec.clone().next().sunday()).toString("yyyy-MM-dd");
    $.post(url, {evento: "get_reservas_can", fe_in: fecha_ini, fe_fin: fecha_fin, id_can: id_cancha}, function (resp) {
        var json = $.parseJSON(resp);
        var fech;
        var fecha;
        var hora;
        var estado;
        $.each(json, function (i, obj) {
            fech = Date.parseExact(obj.FECHA, "yyyy-MM-dd HH:mm:ss");
            fecha = fech.toString("dd/MM/yy");
            hora = fech.toString("HH:mm");
            estado = obj.ESTADO;
            var itens = $("#ordersTable").find("td[data-hora='" + hora + "']");
            $.each(itens, function (i, obje) {
                if ($(obje).data("fecha") == fecha) {
                    $(obje).attr("onclick", "javaScript:void(0)");
                    switch (estado) {
                        case 0: //confirmado

                            break;
                        case 1: //pendiente
                            $(obje).attr("class", "pendiente");
                            break;
                    }


                }
            });

        });

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

function formatAMPM(date) {
    var hours = date.getHours();
    var minutes = date.getMinutes();
    var ampm = hours >= 12 ? 'pm' : 'am';
    hours = hours % 12;
    hours = hours ? hours : 12; // the hour '0' should be '12'
    minutes = minutes < 10 ? '0' + minutes : minutes;
    var strTime = hours + ':' + minutes + ' ' + ampm;
    return strTime;
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

function get_fecha_dia(dia, fecha) {
    switch (dia) {
        case "0":
            return fecha.next().monday();
            break;
        case "1":
            return fecha.next().tuesday();
            break;
        case "2":
            return fecha.next().wednesday();
            break;
        case "3":
            return fecha.next().thursday();
            break;
        case "4":
            return fecha.next().friday();
            break;
        case "5":
            return fecha.next().saturday();
            break;
        case "6":
            return fecha.next().sunday();
            break;
    }

}

function time_format(d) {
    var hours = format_two_digits(d.getHours());
    var minutes = format_two_digits(d.getMinutes());
    return hours + ":" + minutes;
}

function format_two_digits(n) {
    return n < 10 ? '0' + n : n;
}

function siguiente_semana() {
    domingo_selec = domingo_selec.clone().next().sunday();
    cargar_fecha();
}
function anterior_semana() {

    var aux = domingo_selec.clone().last().sunday();
    var dia_ac = Date.today().moveToDayOfWeek(0, -1);

    if (dia_ac.isAfter(aux)) {
        alert("No puede ver reservas anteriores a la fecha");
        return;
    } else {
        domingo_selec = aux;
        cargar_fecha();
    }

}