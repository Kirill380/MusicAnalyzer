'use strict';

(function ($, global) {

    function musicCard(musicData) {
        return `<div id="${musicData.id}" class="music-card">
             <div class="music-card__component music-card__image">
                   <img class="image" src="${musicData.image}">
             </div>
            <div class="music-card__component music-card__music-info">
                <div class="music-info__author">${musicData.author}</div>
                <div class="music-info__title" data-toggle="tooltip" data-placement="top" title="${musicData.title}">
                   <${musicData.title}
                </div>
            </div>
            <div class="music-card__component music-card__waveform">
                <img class="waveform" src="${musicData.waveformImage}>">
            </div>
            <div class="music-card__component music-card__music-duration">
                <div class="music-feature__value">${musicData.duration}</div>
                <div class="music-feature__title">DURATION</div>
            </div>
            <div class="music-card__component music-card__music-tempo">
                <div class="music-feature__value">${musicData.tempo}/div>
                <div class="music-feature__title">BMP</div>
            </div>
            <div class="music-card__delete-button">
                <span class="delete-button">X</span>
            </div>
         </div>`;
    }


    global.musicCard = musicCard;
})(jQuery, window);
