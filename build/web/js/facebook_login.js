/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var url = "indexController";
$(function () {
    esconder_func();

    var scopes = 'email, user_friends, public_profile';

    var btn_login = '<a href="#" id="login" class="btn btn-primary">Iniciar sesión</a>';

    var div_session = "<div id='facebook-session'>" +
            "<strong></strong></br>" +
            "<img></br>" +
            "<a href='#' id='logout' class='btn btn-danger'>Cerrar sesión</a>" +
            "</div>";

    var app_id = '556481944696849';
    window.fbAsyncInit = function () {
        FB.init({
            appId: app_id,
            status: true,
            cookie: true,
            xfbml: true,
            version: 'v2.1'
        });
        FB.getLoginStatus(function (response) {
            statusChangeCallback(response, function () {});
        });
    };

    (function (d, s, id) {
        var js, fjs = d.getElementsByTagName(s)[0];
        if (d.getElementById(id))
            return;
        js = d.createElement(s);
        js.id = id;
        js.src = "//connect.facebook.net/en_US/sdk.js";
        fjs.parentNode.insertBefore(js, fjs);
    }(document, 'script', 'facebook-jssdk'));

    var statusChangeCallback = function (response, callback) {
        console.log(response);

        if (response.status === 'connected') {
            getFacebookData();
        } else {
            callback(false);
        }
    }

    var checkLoginState = function (callback) {
        FB.getLoginStatus(function (response) {
            callback(response);
        });
    };

    var getFacebookData = function () {
        FB.api('/me', function (response) {
            $('#bod_modal_log').html(div_session);
            $('#facebook-session strong').text("Bienvenido: " + response.name);
            $('#facebook-session img').attr('src', 'http://graph.facebook.com/' + response.id + '/picture?type=large');
            $("#loginModal").modal('hide');
            $("#btn_ini_sess").css('display', "none");
            $('#btn_perfil').css('display', "");
            $('.foto_perfil').attr('src', 'http://graph.facebook.com/' + response.id + '/picture?type=large');
            $('.nombre_perfil').text(response.name);
              $('#menu_reserva').css('display', "");
            sessionStorage.setItem("id_log", response.id);
            sessionStorage.setItem("facebook_data",JSON.stringify(response));
        });
    };

    var facebookLogin = function () {
        checkLoginState(function (data) {
            if (data.status !== 'connected') {
                FB.login(function (response) {
                    if (response.status === 'connected')
                        getFacebookData();
                    FB.api('/me', function (response) {
                        $.post(url, {evento: "login_facebook", id: response.id, nombre:response.name}, function (resp) {

                        });
                    });

                }, {scope: scopes});
            }
        });
    };

    var facebookLogout = function () {
        checkLoginState(function (data) {
            if (data.status === 'connected') {
                FB.logout(function (response) {
                    $("#perfil_modal").modal('hide');
                    $("#btn_ini_sess").css('display', "");
                    $('#btn_perfil').css('display', "none");
                       $('#menu_reserva').css('display', "none");
                    sessionStorage.removeItem("id_log");
                    sessionStorage.removeItem("facebook_data");
                    window.location.href="index.html";
                });
            }
        });

    };

    $(document).on('click', '#login', function (e) {
        e.preventDefault();

        facebookLogin();
    });

    $(document).on('click', '#logout', function (e) {
        e.preventDefault();
        facebookLogout();
    });

});

function esconder_func() {
    $("#btn_ini_sess").css('display', "");
    $('#btn_perfil').css('display', "none");
    $('#menu_reserva').css('display', "none");
}


