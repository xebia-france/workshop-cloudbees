define(['pubsub'], function (pubsub) {

    return {

        publish: function () {
            pubsub.publish('SORTING');
        }

    };

});