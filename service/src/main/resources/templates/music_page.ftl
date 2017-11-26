<#import "/spring.ftl" as spring />

<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Music page</title>
    <link rel="stylesheet" href="/vendor/bootstrap-3.3.7/css/bootstrap.css">
    <link rel="stylesheet" href="/vendor/bootstrap-3.3.7/css/bootstrap-theme.css">
    <link rel="stylesheet" href="/vendor/loading-indicator/jquery.loading-indicator.css">
    <link rel="stylesheet" href="/project/styles/music_page.css">
</head>
<body>

<div style="display: none" id="jsMusicId" data-music-id="${musicId}"></div>
<#include "header.ftl"/>
<div class="hero-image jsHeroImage">
    <div class="hero-image__artwork">
        <img src="" alt="image">
    </div>
    <div class="hero-image__title">
        <div class="title__play-button jsPlay">
            <span class="glyphicon glyphicon-play"></span>
        </div>
        <div class="title__author"></div>
        <div class="title__song-name"></div>
    </div>
    <div class="hero-image__waveform">
        <div id="waveform"></div>
    </div>
</div>
<div class="container">
    <div class="section">
        <h2 class="section__header">Music Info</h2>
        <table class="table table-hover jsMusicInfo">
            <thead>
            <tr>
                <th>Attribute</th>
                <th>Value</th>
            </tr>
            </thead>
            <tbody>
            </tbody>
        </table>
    </div>
    <div class="section">
        <h2 class="section__header">Signal waveform</h2>
        <div class="section__controls">
            <input type="text" name="from" class="form-control control__input" value="0" placeholder="from second">
            <input type="text" name="to" class="form-control control__input" value="1" placeholder="to second">
            <button type="button" class="btn btn-info control__button jsTimeSeries">Select</button>
        </div>
        <div id="tsChart" style="min-width: 310px; height: 400px; margin: 0 auto"></div>
    </div>

    <div class="section">
        <h2 class="section__header">Signal spectrogram</h2>
        <div class="section__controls">
            <input type="text" name="from" class="form-control control__input" value="0" placeholder="from second">
            <input type="text" name="to" class="form-control control__input" value="10" placeholder="to second">
            <button type="button" class="btn btn-info control__button jsSpectrogram">Select</button>
        </div>
        <img class="jsSpectrumChart" style="width: 100%; height: 550px;" src="" alt="spectrum">
    </div>

    <div class="section">
        <h2 class="section__header">Predicted genre</h2>
        <table class="table table-hover jsGenreTable">
            <thead>
            <tr>
                <th>Genre</th>
                <th>Percentage</th>
            </tr>
            </thead>
            <tbody>

            </tbody>
        </table>
    </div>

    <div class="controls">
        <button type="button" class="btn btn-danger jsDelete">Delete</button>
        <button type="button" class="btn btn-success jsDownload">Download</button>
    </div>
</div>


<script src="<@spring.url '/vendor/jquery-3.1.1.min.js'/>"></script>
<script src="<@spring.url '/vendor/bootstrap-3.3.7/js/bootstrap.js'/>"></script>
<script src="<@spring.url '/vendor/loading-indicator/jquery.loading-indicator.js'/>"></script>
<script src="<@spring.url '/vendor/jquery.bootstrap-growl.js'/>"></script>
<script src="<@spring.url '/vendor/wavesurfer.min.js'/>"></script>
<script src="<@spring.url '/vendor/color-thief.min.js'/>"></script>
<script src="<@spring.url '/vendor/highcharts/highcharts.js'/>"></script>
<script src="<@spring.url '/vendor/highcharts/modules/exporting.js'/>"></script>
<script src="<@spring.url '/project/js/NotifyUtil.js' />"></script>
<script src="<@spring.url '/project/js/Dictionary.js' />"></script>
<script src="<@spring.url '/project/js/MusicPage.js' />"></script>
</body>
</html>