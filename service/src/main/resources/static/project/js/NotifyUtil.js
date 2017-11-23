'use strict';

(function ($, global) {

    function errorNotification(er) {
        let response = er.responseJSON;
        let errorMessage = response.message;
        $.bootstrapGrowl(errorMessage, {
            ele: 'body',
            type: 'danger',
            offset: {from: 'top', amount: 20},
            align: 'center',
            width: 'auto',
            delay: 4000,
            allow_dismiss: true
        });
    }

    global.errorNotification = errorNotification;

})(jQuery, window);