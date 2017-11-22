$(function () {
    $(document).ready(function () {

        let token = localStorage.getItem('token');
        if(token === null || token.length === 0) {
            window.location.href = "/login";
        }

        $(function () {
            $('[data-toggle="tooltip"]').tooltip();
        });

        $(".jsAdd").on("click", function () {
            $('.jsPopUpMusic').bPopup({
                onClose: function () {

                }
             });
        });

        $.ajax({
                   url: "/api/v1/user/musics",
                   method: "GET",
                   dataType: "json",
                   beforeSend: function (xhr) {
                       xhr.setRequestHeader("Authorization", 'Bearer '+ token)
                   },
                   success: function (audios) {
                       let musicList = $(".jsMusicList");
                       for (let i = 0; i < audios.length; i++) {
                           musicList.append(musicCard(audios[i]));
                       }
                   },
                   error: function (er) {
                       if(er.status === 401) {
                           window.location.href = "/login";
                       }
                   }
               });

        $(".jsAddMusic").on("click", function (event) {
            var $form = $(this).closest("form");
            var formData = new FormData($form[0]);
            var action = $form.attr("action");
            $.ajax({
                       url: action,
                       type: 'POST',
                       data: formData,
                       beforeSend: function (xhr) {
                           xhr.setRequestHeader("Authorization", 'Bearer '+ token)
                       },
                       processData: false,
                       contentType: false,
                       success: function (data) {
                           alert(data);
                           setTimeout(function () {
                               location.reload();
                           }, 2000);
                       },
                       error: function (data) {
                           console.log(data);
                       }
                   });
        });

    });
});
