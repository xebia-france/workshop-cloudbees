define(['async!https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false'], function () {

    // window.google.maps

    return new function () {

        var myMap;
        var markers = [];

        this.addMapToCanvas = function (mapCanvas, lat, lng) {
            var mapOptions = {
                center: new google.maps.LatLng(lat || 48.858165, lng || 2.345186),
                zoom: 8,
                mapTypeId: google.maps.MapTypeId.ROADMAP,
                disableDoubleClickZoom: true
            };

            myMap = new google.maps.Map(mapCanvas, mapOptions);
        }

        this.addMarker = function (lat, lng) {
            var marker = new google.maps.Marker({
                position: new google.maps.LatLng(lat, lng),
                draggable: true,
                map: myMap,
                title: 'Hello'
            });

            markers.push(marker);

        }

        this.removeMarkers = function () {
            for (var markerKey in markers) {
                if (markers.hasOwnProperty(markerKey)) {
                    markers[markerKey].setMap(null);
                }
            }

        }
    }

});





