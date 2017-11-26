(function ($, global) {

    const GENRES_NUMBER = 6;

    $(document).ready(function () {
        let $body = $('body');
        let musicDuration = 0; // in seconds
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
                       musicDuration = musicProfile.duration;
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

        $(".jsTimeSeries").on("click", function () {
            let from = +$(this).siblings("input[name='from']").val();
            let to = +$(this).siblings("input[name='to']").val();
            if(to > musicDuration) {
                errorNotification({responseJSON: {message: `To second [${to}] greater than total duration of track [${musicDuration}]`}});
                return;
            }
            loadWaveform(from, to);
        });


        $(".jsSpectrogram").on("click", function () {
            let from = +$(this).siblings("input[name='from']").val();
            let to = +$(this).siblings("input[name='to']").val();
            if(to > musicDuration) {
                errorNotification({responseJSON: {message: `To second [${to}] greater than total duration of track [${musicDuration}]`}});
                return;
            }
            loadSpectrum(from, to);
        });

        $(".jsLogout").on("click", function () {
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
        });

        $(".jsDelete").on("click", function () {
            $.ajax({
                       url: "/api/v1/user/musics/" + musicId,
                       type: 'DELETE',
                       beforeSend: function (xhr) {
                           xhr.setRequestHeader("Authorization", 'Bearer ' + token);
                           loader.show();
                       },
                       success: function () {
                           loader.hide();
                           setTimeout(function () {
                               window.location.href = "/home"
                           }, 1000)
                       },
                       error: function (er) {
                           loader.hide();
                           errorNotification(er);
                           console.log(er);
                       }
                   });
        });

        function drawInfoTable(musicFeatures) {
            let $musicInfo = $(".jsMusicInfo");
            for (let key in musicFeatures) {
                if (dict.musicInfoTable.hasOwnProperty(key)) {
                    $musicInfo.find("tbody").append(`
                    <tr>
                        <td>${dict.musicInfoTable[key]}</td>
                        <td>${getValue(musicFeatures[key])}</td>
                    </tr>`)
                }
            }
        }

        function getValue(value) {
            return value === null || value.length === 0 ? dict.noData : value
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



        loadWaveform(0, 1);
        loadSpectrum(0, 10);

        function loadWaveform(from, to) {
            $.ajax({
                       url: "/api/v1/user/musics/" + musicId + "/time-series?" + $.param({ from: from, to: to }),
                       method: "GET",
                       dataType: "json",
                       beforeSend: function (xhr) {
                           xhr.setRequestHeader("Authorization", 'Bearer ' + token);
                       },
                       success: function (timeSeries) {
                           Highcharts.chart('tsChart', {
                               title: {
                                   enabled: false,
                                   title: "Signal waveform"
                               },
                               chart: {
                                   type: 'line',
                                   zoomType: 'x'
                               },
                               legend:{
                                   enabled:false
                               },
                               credits: {
                                   enabled: false
                               },
                               series: [{
                                   data: timeSeries.data
                               }]
                           })
                       },
                       error: function (er) {
                           errorNotification(er);
                           console.log(er);
                           if (er.status === 401) {
                               window.location.href = "/login";
                           }
                           if (er.status === 404) {
                               window.location.href = "/home";
                           }
                       }
                   });
        }

        function loadSpectrum(from, to) {
            $.ajax({
                       url: "/api/v1/user/musics/" + musicId + "/spectrogram?" + $.param({ from: from, to: to }),
                       method: "GET",
                       dataType: "json",
                       beforeSend: function (xhr) {
                           xhr.setRequestHeader("Authorization", 'Bearer ' + token);
                       },
                       success: function (spectrum) {
                           let $spectrumChart = $(".jsSpectrumChart");
                           $spectrumChart.attr("src", spectrum.image);
                       },
                       error: function (er) {
                           errorNotification(er);
                           console.log(er);
                           if (er.status === 401) {
                               window.location.href = "/login";
                           }
                           if (er.status === 404) {
                               window.location.href = "/home";
                           }
                       }
                   });
        }



        var ctx = document.createElement('canvas').getContext('2d');
        var linGrad = ctx.createLinearGradient(0, 64, 0, 200);
        linGrad.addColorStop(0.5, 'rgba(255, 255, 255, 1.000)');

        linGrad.addColorStop(0.5, 'rgba(183, 183, 183, 1.000)');

        var wavesurfer = WaveSurfer.create({
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
