define(['pubsub'], function (pubsub) {

    return {

        subscribe: function () {
            pubsub.subscribe('SORTING', function (message) {
                console.log(message);
            });
        }

    };

});