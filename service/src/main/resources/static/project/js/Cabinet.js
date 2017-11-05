$(function () {
    $(document).ready(function () {
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
                   success: function (audios) {
                       let musicList = $(".jsMusicList");
                       for (let i = 0; i < audios.length; i++) {
                           musicList.append(musicCard(audios[i]));
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
