/**
 * Created by alex on 28.03.15.
 */
"use strict";
$(function () {

    uiFeatures.init();
    mapHome.init();
    playersHome.showTopNine();


});


var playersHome = (function () {

    var $this = {};

    $this.showTopNine = function () {
        getData('http://private-anon-2879e03fd-heap.apiary-mock.com/users/top', 'get', {}, function (topPlayers) {
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
                '<img style="border-radius:50px" src="{{properties.avatar}}">'
            );

            getData('http://private-anon-2879e03fd-heap.apiary-mock.com/users/location', 'get', {}, function (data) {
                var users = data.users;
                for (var i = 0; i < users.length; i++) {
                    $this.map.geoObjects.add(new ymaps.Placemark([users[i].latitude, users[i].longitude], {
                        avatar: users[i].avatar
                    }, {
                        hintLayout: userBalloonLayout
                    }));
                }
            })
        });

    };


    return $this;
}());

var uiFeatures = (function () {

    var $this = {};

    $this.init = function(){

        $(".button-collapse").sideNav();
        $this.correctSectionHeight();
        $( window ).resize(function() {
            $this.correctSectionHeight();
        });
        $this.initWaypoints();
    };

    $this.correctSectionHeight = function () {
        var footerHeight = document.getElementsByTagName("footer")[0].offsetHeight;
        var headerHeight = document.getElementsByTagName("header")[0].offsetHeight;
        var sections = document.getElementsByClassName("land");
        for (var i = 0; i < sections.length; i++) {
            if(sections[i].offsetHeight < window.innerHeight - headerHeight) {
                sections[i].style.height = window.innerHeight - headerHeight -50 + 'px';
            }
        }
        //if(sections[sections.length - 1].offsetHeight < window.innerHeight - headerHeight -footerHeight) {
        //    sections[sections.length - 1].style.height = window.innerHeight - footerHeight - headerHeight + 'px';
        //}
    };



    $this.initWaypoints = function () {
        //$('.sticky-nav').waypoint('sticky');
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