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

        $(".jsAdd").on("click", function () {
            $('.jsPopUpMusic').bPopup({});
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
                           alert(data);
                           setTimeout(function () {
                               location.reload();
                           }, 2000);
                       },
                       error: function (er) {
                           loader.hide();
                           alert(er.toString());
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
                           musicList.empty()
                           for (let i = 0; i < items.length; i++) {
                               musicList.append(musicCard(items[i]));
                           }
                           refreshPaginator(Math.ceil(audios.totalCount / ITEMS_PER_PAGE));
                       },
                       error: function (er) {
                           loader.hide();
                           if (er.status === 401) {
                               window.location.href = "/login";
                           }
                           alert(er.toString());
                       }
                   });
        }


        function refreshPaginator(totalPages) {
            if(totalPages < 1) {
                $pagination.twbsPagination(paginationDefaultOpts);
                $pagination.hide();
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
