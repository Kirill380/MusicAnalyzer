$(function () {
    $(document).ready(function () {

        $(".jsLogin").on("click", function (event) {
            var $form = $(this).closest("form");
            var $inputs = $form.find(':input:not(.btn)');
            var values = {};
            $inputs.each(function () {
                values[this.name] = $(this).val();
            });
            $.ajax({
                       url: $form.attr("action"),
                       type: 'POST',
                       data: JSON.stringify(values),
                       contentType: "application/json",
                       success: function (data) {
                           localStorage.setItem("token", data.token);
                           setTimeout(function () {
                               window.location.href = "/home"
                           }, 1000);
                       },
                       error: function (er) {
                           errorNotification(er);
                           console.log(er);
                       }
                   });
        });


    });
});
