$(function () {
    esconder_func();
    var app_id = '556481944696849';
    var scopes = 'email, user_friends, public_profile';

    var btn_login = '<a href="#" id="login" class="btn btn-primary">Iniciar sesión</a>';

    var div_session = "<div id='facebook-session'>" +
            "<strong></strong>" +
            "<img>" +
            "<a href='#' id='logout' class='btn btn-danger'>Cerrar sesión</a>" +
            "</div>";

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
            $('#login').after(div_session);
            $('#login').remove();
            $('#facebook-session strong').text("Bienvenido: " + response.name);
            $('#facebook-session img').attr('src', 'http://graph.facebook.com/' + response.id + '/picture?type=large');
            $("#loginModal").modal('hide');
            $("#btn_ini_sess").css('display', "none");
            $('#btn_perfil').css('display', "");
            $('.foto_perfil').attr('src', 'http://graph.facebook.com/' + response.id + '/picture?type=large');
            $('.nombre_perfil').text(response.name);
        });
    };

    var facebookLogin = function () {
        checkLoginState(function (data) {
            if (data.status !== 'connected') {
                FB.login(function (response) {
                    if (response.status === 'connected')
                        getFacebookData();
                }, {scope: scopes});
            }
        });
    };

    var facebookLogout = function () {
        checkLoginState(function (data) {
            if (data.status === 'connected') {
                FB.logout(function (response) {
                    $('#facebook-session').before(btn_login);
                    $('#facebook-session').remove();
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

        if (confirm("¿Está seguro?"))
            facebookLogout();
        else
            return false;
    });

});

function esconder_func() {
    $("#btn_ini_sess").css('display', "");
    $('#btn_perfil').css('display', "none");
}


