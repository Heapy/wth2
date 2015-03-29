/**
 * Created by alex on 28.03.15.
 */
"use strict";

var baseUrl = "http://heap.by";

$(function () {

    uiFeatures.init();
    mapHome.init();
    playersHome.showTopNine();
});

var authHome = (function () {

    var $this = {};
    $this.token = {};
    $this.currentUser = {
        id: 1,
        avatar: "http://heap.by/assets/avatars/6.jpg",
        username: "Kulebyaka@email.com",
        karma: 9,
        displayName: "Odin Rom"
    };

    $this.register = function () {
        $.ajax({
            url: baseUrl + '/auth/register',
            contentType: 'application/json; charset=UTF-8',
            method: 'post',
            data: JSON.stringify({
                username: $("#username").val(),
                password: $("#password").val(),
                displayName: $("#displayName").val()
            })
        }).done(function (data) {
            $this.token = JSON.parse(data).token;
        });
        return false;
    };

    $this.login = function () {
        succesLogin({});
        //$.ajax({
        //    url: baseUrl + '/auth/login',
        //    contentType: 'application/json; charset=UTF-8',
        //    method: 'post',
        //    data: JSON.stringify({
        //        username: $("#login-username").val(),
        //        password: $("#login-password").val()
        //    })
        //}).done(function (data) {
        //    $this.token = JSON.parse(data).token;
        //    $.ajax({
        //        headers: $this.token,
        //        method: 'get',
        //        url: baseUrl + '/user'
        //    }).done(succesLogin);
        //})
    };

    function succesLogin(data) {
        $("#logout-button").show();
        showProfile(data);
    }

    function showProfile(data) {
        var user = $this.currentUser;
        $("#auth").hide();
        $("#profile-view").show();
        $(".profile-avatar img").attr("src", user.avatar);
        $(".profile-username span").html(user.username);
        $(".profile-displayName span").html(user.displayName);
        $(".profile-karma span").html(user.karma + 'Points');
        if ($this.currentUser.id === user.id) {
            $(".profile-edit").show();
            $("#edit-username").val(user.username);
            $("#edit-displayName").val(user.displayName);
        } else {
            $(".profile-edit").hide();
        }
        scrollTo('profile', 100);
    }

    $this.logout = function () {
        $("#logout-button").hide();
        $("#profile-view").hide();
        $("#auth").show();
        //$this.currentUser = {};
        //$this.token = {};
        return false;
    };

    return $this;

}());


var playersHome = (function () {

    var $this = {};

    $this.showTopNine = function () {
        getData(baseUrl + '/users/top', 'get', {}, function (topPlayers) {
            var template = $('#player-short-template').html();

            var html = Mustache.to_html(template, {users: topPlayers});
            $('#players').html(html);
            uiFeatures.correctSectionHeight();
        })
    };

    return $this;

}());

var mapHome = (function () {

    var $this = {};
    $this.map = {};
    $this.points = {};
    $this.template = '';
    $this.showingUsers = {};

    $this.init = function () {
        ymaps.ready(function () {
            $this.map = new ymaps.Map("map", {
                center: [53.903063, 27.561288],
                zoom: 11,
                controls: []
            });
            $this.map.behaviors.disable(['drag', 'rightMouseButtonMagnifier', 'scrollZoom']);

            $this.template = ymaps.templateLayoutFactory.createClass(
                '<img style="border-radius:50px; width: 80px; height: 80px;" src="{{properties.avatar}}">' +
                '<div style="width:90px;height: 90px;border-radius: 50px; background: white; z-index: -5; position: relative; top: -85px; left: -5px"></div>' +
                '<span style="background-color: white;font-size: 16px;box-shadow: -1px -1px 15px 0px rgba(50, 50, 50, 0.75); display:inline-block; text-align:center; padding: 5px 7px; width:90px; position: relative; top: -75px; left: -10px">{{properties.displayName}}</span>'
            );


            $this.points = new ymaps.GeoObjectCollection({}, {
                preset: 'default#image',
                draggable: false
            });

            $this.map.geoObjects.add($this.points);

            setInterval($this.update, 3000);


        });

        $this.update = function () {
            getData(baseUrl + '/users/location', 'get', {}, function (data) {
                var users = data;
                for (var i = 0; i < users.length; i++) {
                    var oldUser = $this.showingUsers[users[i].id];
                    if (oldUser && oldUser.latitude === users[i].latitude && oldUser.longitude === users[i].longitude) {
                        continue;
                    }
                    var newPoint = getPoint(users[i]);
                    if(oldUser){
                        $this.points.remove(oldUser.point);
                    } else {
                        $this.showingUsers[users[i].id] = users[i];
                        oldUser = $this.showingUsers[users[i].id];

                    }
                    oldUser.point = newPoint;
                    oldUser.latitude = users[i].latitude;
                    oldUser.longitude = users[i].longitude;
                    $this.points.add(newPoint)
                }
            })
        }

    };

    function getPoint(user) {
        return new ymaps.Placemark([user.latitude, user.longitude], {
            avatar: user.avatar,
            displayName: user.displayName
        }, {
            hintLayout: $this.template,
            iconLayout: 'default#image',
            iconImageHref: "assets/img/ico.png",
            iconImageSize: [40, 40]
        });
    }


    return $this;
}());

var uiFeatures = (function () {

    var $this = {};

    $this.init = function () {

        $this.correctSectionHeight();
        $(window).resize(function () {
            $this.correctSectionHeight();
        });
    };


    $this.correctSectionHeight = function () {
        var headerHeight = document.getElementsByTagName("header")[0].offsetHeight;
        var sections = document.getElementsByClassName("land");
        for (var i = 0; i < sections.length; i++) {
            sections[i].style.height = 'inherit';
            if (sections[i].offsetHeight < window.innerHeight - headerHeight - 50) {
                sections[i].style.height = window.innerHeight - headerHeight - 50 + 'px';
            }
        }
    };

    return $this;

}());

function getData(url, method, params, callBack) {
    $.ajax({
        method: method,
        url: url,
        data: params
    }).done(callBack);
}