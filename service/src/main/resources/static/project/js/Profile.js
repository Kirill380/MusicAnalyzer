$(function () {
    $(document).ready(function () {
        let $body = $('body');
        let userId;
        let token = localStorage.getItem('token');
        let language = localStorage.getItem('language') || "en";

        if (token === null || token.length === 0) {
            window.location.href = "/login";
        }

        $body.loadingIndicator();
        let loader = $body.data("loadingIndicator");


        $.ajax({
                   url: "/api/v1/users/current",
                   method: "GET",
                   dataType: "json",
                   beforeSend: function (xhr) {
                       xhr.setRequestHeader("Authorization", 'Bearer ' + token);
                       loader.show();
                   },
                   success: function (userProfile) {
                       loader.hide();
                       let $inputs = $(".jsUserInfo").find(':input:not(.btn)');
                       $inputs.each(function () {
                           $(this).val(userProfile[this.name]);
                       });
                       userId = userProfile.id;
                   },
                   error: function (er) {
                       loader.hide();
                       if (er.status === 401) {
                           window.location.href = "/login";
                       }
                       errorNotification(er);
                       console.log(er);
                   }
               });

        $(".jsChangeUserInfo").on("click", function (event) {
            var $form = $(this).closest("form");
            var $inputs = $form.find(':input:not(.btn)');
            var values = {};
            $inputs.each(function () {
                values[this.name] = $(this).val();
            });
            $.ajax({
                       url: $form.attr("action").replace(/{.*}/, userId),
                       type: 'PUT',
                       data: JSON.stringify(values),
                       contentType: "application/json",
                       beforeSend: function (xhr) {
                           xhr.setRequestHeader("Authorization", 'Bearer ' + token);
                       },
                       success: function (data) {
                           setTimeout(function () {
                               location.reload();
                           }, 1000);
                       },
                       error: function (er) {
                           errorNotification(er);
                           console.log(er);
                       }
                   });
        });


        $(".jsChangePassword").on("click", function (event) {
            var $form = $(this).closest("form");
            var $inputs = $form.find(':input:not(.btn)');
            var values = {};
            $inputs.each(function () {
                values[this.name] = $(this).val();
            });
            $.ajax({
                       url: $form.attr("action").replace(/{.*}/, userId),
                       type: 'PUT',
                       data: JSON.stringify(values),
                       contentType: "application/json",
                       beforeSend: function (xhr) {
                           xhr.setRequestHeader("Authorization", 'Bearer ' + token);
                       },
                       success: function (data) {
                           setTimeout(function () {
                               logOut();
                           }, 1000);
                       },
                       error: function (er) {
                           errorNotification(er);
                           console.log(er);
                       }
                   });
        });

        $(".jsLogout").on("click", function () {
            logOut();
        });


        function logOut() {
            if (token === null || token.length === 0) {
                window.location.href = "/login";
            }
            localStorage.removeItem('token');
            let data = {accessToken: token};
            token = null;
            $.ajax({
                       url: "/auth/logout",
                       type: 'POST',
                       data: JSON.stringify(data),
                       contentType: "application/json",
                       success: function () {
                           window.location.href = "/login"
                       },
                       error: function (er) {
                           errorNotification(er);
                           console.log(er);
                       }
                   });
        }
    });
});
