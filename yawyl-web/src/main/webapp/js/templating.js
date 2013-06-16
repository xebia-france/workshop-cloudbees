define(['handlebars', 'text!templates/template.html'], function (handlebars, template) {

    return function () {

        handlebars.registerPartial('template', handlebars.compile(template));

        handlebars.registerHelper('equalTo', function (arg1, arg2, options) {
            if (arg1 == arg2) {
                return options.fn(this);
            } else {
                return options.inverse(this);
            }
        });

        var template = function (template, json) {
            var partial = handlebars.partials[template];
            return partial(json);
        };


    };

});