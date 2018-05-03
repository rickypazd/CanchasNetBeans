/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var url = "adminController";
var id_cancha;
$(document).ready(function () {
    $("#registro_2").css("display", "none");
    id_cancha = sessionStorage.getItem("id_cancha");
    $.post(url, {evento: "get_tipos_canchas"}, function (resp) {
        var tipos_canchas = $.parseJSON(resp);
        var html = "";
        $.each(tipos_canchas, function (i, obj) {
            html += "<option value='" + obj.ID + "'>" + obj.TIPO + "</option>";
        });
        $("#select_tipo").append(html);
        cargar_tabla_costos(id_cancha);
    });
});

function add_caracter() {
    $("#div_caracteristicas").append("<input type='text' class='form-control' placeholder='Caracteristica'>");
}
var nombre_cancha;
var tipo_cancha;
function siguiente() {
    var nombre = $("#nombre").val() || 0;
    var tipo = $("#select_tipo").val() || 0;

    $("#nombre").css("background", "#ffffff");
    $("#select_tipo").css("background", "#ffffff");

    if (nombre <= 0) {
        $("#nombre").css("background", "#F09C84");
        $("#nombre").focus();
        return;
    }
    if (tipo == "0") {
        $("#select_tipo").css("background", "#F09C84");
        $("#select_tipo").focus();
        return;
    }
    nombre_cancha = nombre;
    tipo_cancha = tipo;
    $("#registro_1").css("display", "none");
    $("#registro_2").css("display", "");
}

function atras() {
    $("#registro_1").css("display", "");
    $("#registro_2").css("display", "none");
}

function cargar_tabla_costos(id) {
    $.post(url, {evento: "get_horario_complejo", id: id}, function (resp) {
        var json = $.parseJSON(resp);
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
                html += "<th class='" + obj.DIA + "'>" + get_dia(obj.DIA) + "</th>";
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
                    html += "<td contenteditable='true'  data-dia='" + hora_formateada + "' class='" + obj.DIA + "' onkeypress='javascript:return isNumber(event)'></td>";
                } else {
                    html += "<td style='background:#000000'></td>";
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
                        html += "<td contenteditable='true'  class='" + obj.DIA + "' data-dia='" + hora_formateada + "' onkeypress='javascript:return isNumber(event)' ></td>";
                        ;
                    } else {
                        html += "<td style='background:#000000'></td>";
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
                    html += "<td contenteditable='true' class='" + obj.DIA + "' data-dia='" + hora_formateada + "' onkeypress='javascript:return isNumber(event)'></td>";
                } else {
                    html += "<td style='background:#000000'></td>";
                }
            }
        });
        html += "</tr>";
        horas--;
        $("#tabla_cost").html(html);

    });
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

function crear() {
    var valido = true;
    var dia;
    var json = '[';
    $("#tabla_cost tr th").each(function (i, obj) {
        dia = $(obj).attr("class");
        var horas;
        var hora;
        var precio;

        if (dia != null) {
            horas = $("#tabla_cost").find("." + dia);
            $.each(horas, function (i, obj) {
                hora = $(obj).data("dia");
                if (hora != null) {
                    precio = $(obj).text();
                    if (precio.length > 0) {
                        json += '{"dia":' + dia + ',"hora":"' + hora + '","precio":"' + precio + '"},';
                        $(obj).css("background", "#ffffff");
                    } else {
                        $(obj).css("background", "#636363");
                        valido = false;
                    }
                }
            });
        }

    });
    json = json.substring(0, json.length - 1);
    json += ']';

    if (valido) {
        $.post(url, {evento: "agg_cancha",
            id_complejo: id_cancha,
            nombre: nombre_cancha,
            tipo: tipo_cancha,
            costos: json}, function (resp) {
            if (resp == 'exito') {
                   change_frame_padre("ver_cancha_admin");
            }
        });
    }
}

function isNumber(evt) {
    var iKeyCode = (evt.which) ? evt.which : evt.keyCode
    if (iKeyCode != 46 && iKeyCode > 31 && (iKeyCode < 48 || iKeyCode > 57))
        return false;

    return true;
}
function time_format(d) {
    var hours = format_two_digits(d.getHours());
    var minutes = format_two_digits(d.getMinutes());
    return hours + ":" + minutes;
}

function format_two_digits(n) {
    return n < 10 ? '0' + n : n;
}
function change_frame_padre(dir) {
    window.parent.change_frame(dir);
}