/**
 * Created by alex on 28.03.15.
 */
"use strict";

var baseUrl = "http://private-anon-2879e03fd-heap.apiary-mock.com";

$(function () {

    uiFeatures.init();
    mapHome.init();
    playersHome.showTopNine();


});


var playersHome = (function () {

    var $this = {};

    $this.showTopNine = function () {
        getData(baseUrl + '/users/top', 'get', {}, function (topPlayers) {
            var template = $('#player-short-template').html();
            var html = Mustache.to_html(template, topPlayers);
            $('#players').html(html);
        })
    };

    return $this;

}());

var mapHome = (function () {

    var $this = {};
    $this.map = {};
    $this.points = [];

    $this.init = function () {
        ymaps.ready(function () {
            $this.map = new ymaps.Map("map", {
                center: [53.903063, 27.561288],
                zoom: 11,
                controls: []
            });
            $this.map.behaviors.disable(['drag', 'rightMouseButtonMagnifier', 'scrollZoom']);

            var userBalloonLayout = ymaps.templateLayoutFactory.createClass(
                '<img style="border-radius:50px" src="{{properties.avatar}}">' +
                '<div style="width:90px;height: 90px;border-radius: 50px; background: white; z-index: -5; position: relative; top: -85px; left: -5px"></div>' +
                '<span style="background-color: white;font-size: 16px;box-shadow: -1px -1px 15px 0px rgba(50, 50, 50, 0.75); display:inline-block; text-align:center; padding: 5px 7px; width:90px; position: relative; top: -75px; left: -10px">{{properties.displayName}}</span>'
            );

            getData(baseUrl + '/users/location', 'get', {}, function (data) {
                var users = data.users;
                for (var i = 0; i < users.length; i++) {
                    $this.map.geoObjects.add(new ymaps.Placemark([users[i].latitude, users[i].longitude], {
                        avatar: users[i].avatar,
                        displayName: users[i].displayName
                    }, {
                        hintLayout: userBalloonLayout,
                        iconLayout: 'default#image',
                        iconImageHref: "assets/img/ico.png",
                        iconImageSize: [40, 40]
                    }));
                }
            })
        });

    };


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