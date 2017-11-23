$(function () {

    const ITEMS_PER_PAGE = 3;

    $(document).ready(function () {
        let $body = $('body');
        let $pagination = $("#jsPagination");

        let token = localStorage.getItem('token');
        if (token === null || token.length === 0) {
            window.location.href = "/login";
        }

        $body.loadingIndicator();
        let loader = $body.data("loadingIndicator");

        loadProfiles(1);

        let paginationDefaultOpts = {
            totalPages: 1,
            visiblePages: 5,
            initiateStartPageClick: false,
            onPageClick: function (event, page) {
                loadProfiles(page);
            }
        };


        $('[data-toggle="tooltip"]').tooltip();

        $(".jsAdd").on("click", function (event) {
            event.preventDefault();
            $('.jsPopUpMusic').bPopup({});
        });



        $(".jsLogout").on("click", function () {
            if (token === null || token.length === 0) {
                window.location.href = "/login";
            }
            localStorage.removeItem('token');
            let data = { accessToken : token };
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
                           xhr.setRequestHeader("Authorization", 'Bearer ' + token);
                           loader.show();
                       },
                       processData: false,
                       contentType: false,
                       success: function (data) {
                           loader.hide();
                           uploadedNotification();
                           setTimeout(function () {
                               location.reload();
                           }, 2000);
                       },
                       error: function (er) {
                           loader.hide();
                           errorNotification(er);
                           console.log(er);
                       }
                   });
        });


        function loadProfiles(page, title) {
            $.ajax({
                       url: "/api/v1/user/musics?" + $.param({ page: page, title: title }),
                       method: "GET",
                       dataType: "json",
                       beforeSend: function (xhr) {
                           xhr.setRequestHeader("Authorization", 'Bearer ' + token);
                           loader.show();
                       },
                       success: function (audios) {
                           loader.hide();
                           let musicList = $(".jsMusicList");
                           let items = audios.items;
                           musicList.empty();
                           for (let i = 0; i < items.length; i++) {
                               musicList.append(musicCard(items[i]));
                           }
                           addDeleteHandler();
                           refreshPaginator(Math.ceil(audios.totalCount / ITEMS_PER_PAGE));
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
        }

        function uploadedNotification() {
            $.bootstrapGrowl("Music was successfully uploaded", {
                ele: 'body',
                type: 'success',
                offset: {from: 'top', amount: 20},
                align: 'center',
                width: 'auto',
                delay: 4000,
                allow_dismiss: true
            });
        }

        function addDeleteHandler() {
            $(".jsDelete").on("click", function () {
                let musicId = $(this).parent().parent().get(0).id;
                $.ajax({
                           url: "/api/v1/user/musics/" + musicId,
                           type: 'DELETE',
                           beforeSend: function (xhr) {
                               xhr.setRequestHeader("Authorization", 'Bearer ' + token);
                               loader.show();
                           },
                           success: function () {
                               setTimeout(function () {
                                   location.reload();
                               }, 2000)
                           },
                           error: function (er) {
                               errorNotification(er);
                               console.log(er);
                           }
                       });
            });
        }

        function refreshPaginator(totalPages) {
            if(totalPages <= 1) {
                $pagination.twbsPagination(paginationDefaultOpts);
                return;
            }
            $pagination.show();
            let currentPage = $pagination.twbsPagination('getCurrentPage');
            $pagination.twbsPagination('destroy');
            $pagination.twbsPagination($.extend({}, paginationDefaultOpts, {
                startPage: currentPage,
                totalPages: totalPages
            }));
        }

    });
});
