/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var url = "indexController"; //
var complejo;
$(document).ready(function () {

    var id_complejo = getQueryVariable("id");
    $.post(url, {evento: "get_complejos_id", id: id_complejo}, function (resp) {
        complejo = $.parseJSON(resp);
        $("#tv_nombre_complejo").html(complejo.ID + " gola");
        $("#idParafo1_PerfilComplejo").html(complejo.NOMBRE + " dsdsd");

        if (complejo.B64 != "") {
            $("#idFoto_PerfilComplejo").attr("src", complejo.B64);
        } else {
            $("#idFoto_PerfilComplejo").attr("src", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTxyjuzhER45dRNfKZn24IZB_jLm-vK19mY62ID_dpuPRV7ldwZ");
        }

        //$("#idFoto_PerfilComplejo").attr("https://ptetutorials.com/images/user-profile.png");


    });






});



function mostrarlista() {
    var x = document.getElementById("idBubble");
    if (x.style.display === "none") {
        x.style.display = "block";
    } else {
        x.style.display = "none";
    }
}

function getQueryVariable(varia) { //
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