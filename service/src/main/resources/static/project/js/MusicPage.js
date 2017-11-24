(function ($, global) {

    const GENRES_NUMBER = 6;

    $(document).ready(function () {
        let $body = $('body');
        const colorThief = new ColorThief();
        const musicId = $("#jsMusicId").attr("data-music-id");

        let language = localStorage.getItem('language') || "en";
        const dict = dictionary[language];

        let token = localStorage.getItem('token');
        if (token === null || token.length === 0) {
            window.location.href = "/login";
        }

        $body.loadingIndicator();
        let loader = $body.data("loadingIndicator");

        $.ajax({
                   url: "/api/v1/user/musics/" + musicId + "/metadata",
                   method: "GET",
                   dataType: "json",
                   beforeSend: function (xhr) {
                       xhr.setRequestHeader("Authorization", 'Bearer ' + token);
                       loader.show();
                   },
                   success: function (musicProfile) {
                       loader.hide();
                       let $heroImage = $(".jsHeroImage");
                       $heroImage.find(".title__author").html(musicProfile.author);
                       $heroImage.find(".title__song-name").html(musicProfile.title);
                       let $image = $heroImage.find(".hero-image__artwork img");
                       $image.attr("src", musicProfile.image);
                       $image.on("load", function () {
                           let paletteArray = colorThief.getPalette(this, 2);
                           $heroImage.css("background",
                                          "linear-gradient(135deg, rgb(" + paletteArray[0].join(',')
                                          + ") 0%, rgb(" + paletteArray[1].join(',') + ") 100%)");
                       });
                       drawInfoTable(musicProfile);
                       drawGenreTable(JSON.parse(musicProfile.predictedGenres))
                   },
                   error: function (er) {
                       loader.hide();
                       if (er.status === 401) {
                           window.location.href = "/login";
                       }
                       if (er.status === 404) {
                           window.location.href = "/home";
                       }
                       errorNotification(er);
                       console.log(er);
                   }
               });

        function drawInfoTable(musicFeatures) {
            let $musicInfo = $(".jsMusicInfo");
            for (let key in musicFeatures) {
                if (dict.musicInfoTable.hasOwnProperty(key)) {
                    $musicInfo.find("tbody").append(`
                    <tr>
                        <td>${dict.musicInfoTable[key]}</td>
                        <td>${musicFeatures[key]}</td>
                    </tr>`)
                }
            }

        }

        function drawGenreTable(genresPredictions) {
            let $genreTable = $(".jsGenreTable");
            let percentSum = 0;
            for (let i = 0; i < GENRES_NUMBER; i++) {
                percentSum += parseFloat(genresPredictions[i][1]);
                $genreTable.find("tbody").append(`
                  <tr>
                      <td>${genresPredictions[i][0]}</td>
                      <td>${(parseFloat(genresPredictions[i][1]) * 100).toFixed(2) + "%"}</td>
                  </tr>`)
            }

            $genreTable.find("tbody").append(`
                  <tr>
                      <td>Other genres</td>
                      <td>${((1 - percentSum) * 100).toFixed(2) + "%"}</td>
                  </tr>`)

        }

        $.getJSON("/api/v1/user/musics/" + musicId + "/time-series",
                  function (data) {

                      Highcharts.chart('tsChart', {})

                  });

        var ctx = document.createElement('canvas').getContext('2d');
        var linGrad = ctx.createLinearGradient(0, 64, 0, 200);
        linGrad.addColorStop(0.5, 'rgba(255, 255, 255, 1.000)');

        linGrad.addColorStop(0.5, 'rgba(183, 183, 183, 1.000)');

        var wavesurfer = WaveSurfer.create({
                                               // Use the id or class-name of the element you
                                               // created, as a selector
                                               container: '#waveform',
                                               // The color can be either a simple CSS color or a
                                               // Canvas gradient
                                               waveColor: linGrad,
                                               progressColor: 'orange',
                                               cursorWidth: 0,
                                               barWidth: 3
                                           });

        wavesurfer.load(window.location.origin + '/api/v1/user/musics/' + musicId + "/audio-file");

        $(".jsPlay").on("click", function () {
            wavesurfer.playPause();
        })

    });
})(jQuery, window);
