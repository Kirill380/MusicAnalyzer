import librosa
import mutagen
from music_classifier import genrePrediction
from waveform_generator import generate_tsImage
import json

MUSIC_FILES_STORE = "/var/lib/music-analyzer/music/store"

FILE_NAME = "musicFile"
ART_IMAGE = "artImage"
TS_IMAGE = "tsImage"


def retrieve_features(music_id):
    file_name = MUSIC_FILES_STORE + '/' + music_id + '/' + FILE_NAME
    audio = mutagen.File(file_name)
    y, sr = librosa.load(file_name)

    print "Extracting artwork image"

    art_work_tag = audio.tags.get('APIC:')
    if art_work_tag is not None:
        with open(MUSIC_FILES_STORE + '/' + music_id + '/' + ART_IMAGE + '.jpg', 'wb') as img:
            img.write(art_work_tag.data)
    else:
        print "Art work image is absent"

    generate_tsImage(file_name, MUSIC_FILES_STORE + '/' + music_id + '/' + TS_IMAGE + '.jpg')

    onset_env = librosa.onset.onset_strength(y, sr=sr)
    tempo = librosa.beat.tempo(onset_envelope=onset_env, sr=sr)
    print "Tempo of music is %d" % tempo
    predictedGenres = genrePrediction(filePath=file_name)
    return {
        'tempo': tempo[0],
        'predictedGenres': json.dumps(predictedGenres),
        'title': getValue(audio.get('TIT2'), "title"),
        'author': getValue(audio.get('TPE1'), "author"),
        'album': getValue(audio.get('TALB'), "album"),
        'yearRecorded': getValue(audio.get('TDRC'), "yearRecorded"),
        'genre': getValue(audio.get('TCON'), "genre"),
        'channels': audio.info.channels,
        'duration': audio.info.length,
        'sampleRate': audio.info.sample_rate
    }


def getValue(tag, feature_name):
    if tag is None:
        return ""
    else:
        value = tag.text[0].encode("utf-8")
        print "Value for feature " + feature_name + " is " + value
        return value
