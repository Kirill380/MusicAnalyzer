import librosa
import librosa.display
import mutagen
from music_classifier import genrePrediction
from waveform_generator import generate_tsImage
import json
import numpy as np
import base64
import io
import matplotlib.pyplot as plt


MUSIC_FILES_STORE = "/var/lib/music-analyzer/music/store"

FILE_NAME = "musicFile"
ART_IMAGE = "artImage"
TS_IMAGE = "tsImage"
DEFAULT_SAMPLE_RATE = 44100
DEFAULT_INTERVAL = 20E-6

ATTR_DICT = {
    'title': 'TIT2',
    'author': 'TPE1',
    'album': 'TALB',
    'yearRecorded': 'TDRC',
    'genre': 'TCON'
}


def retrieve_features(music_id):
    file_name = MUSIC_FILES_STORE + '/' + music_id + '/' + FILE_NAME
    y, sr = librosa.load(file_name, sr=None)
    feature_map = {}
    get_music_metadata(file_name, music_id, feature_map)

    if feature_map.get('duration') is None:
        feature_map['duration'] = librosa.get_duration(y=y, sr=sr)

    if feature_map.get('sampleRate') is None:
        feature_map['sampleRate'] = sr

    generate_tsImage(file_name, MUSIC_FILES_STORE + '/' + music_id + '/' + TS_IMAGE + '.jpg')

    onset_env = librosa.onset.onset_strength(y, sr=sr)

    tempo = librosa.beat.tempo(onset_envelope=onset_env, sr=sr)
    print "Tempo of music is %d" % tempo
    feature_map['tempo'] = tempo[0]

    predicted_genres = genrePrediction(filePath=file_name)
    feature_map['predictedGenres'] = json.dumps(predicted_genres)
    return feature_map


def get_time_series(music_id, from_sec, to_sec):
    file_name = MUSIC_FILES_STORE + '/' + music_id + '/' + FILE_NAME
    ts, sr = librosa.load(file_name, sr=None, offset=from_sec, duration=to_sec - from_sec)
    return {
        "data": [[i * DEFAULT_INTERVAL, x.item()] for i, x in enumerate(ts)]
    }


def get_spectrum(music_id, from_sec, to_sec):
    file_name = MUSIC_FILES_STORE + '/' + music_id + '/' + FILE_NAME
    ts, sr = librosa.load(file_name, sr=None, offset=from_sec, duration=to_sec - from_sec)
    D = librosa.stft(ts, center=False)
    plt.figure(figsize=(16, 8))
    librosa.display.specshow(librosa.amplitude_to_db(D, ref=np.max), y_axis='linear', x_axis='time')
    plt.colorbar(format='%+2.0f dB')
    plt.tight_layout()
    plt.draw()
    img_buffer = io.BytesIO()
    plt.savefig(img_buffer, format='jpg')
    img_buffer.seek(0)
    return {
        "image": "data:image/jpg;base64," + base64.b64encode(img_buffer.getvalue())
    }


def get_music_metadata(file_name, music_id, feature_map):
    audio = mutagen.File(file_name)
    if audio is not None:
        print "Extracting artwork image"
        art_work_tag = audio.tags.get('APIC:')
        if art_work_tag is not None:
            with open(MUSIC_FILES_STORE + '/' + music_id + '/' + ART_IMAGE + '.jpg', 'wb') as img:
                img.write(art_work_tag.data)
        else:
            print "Art work image is absent"

        feature_map['channels'] = audio.info.channels
        feature_map['duration'] = audio.info.length
        feature_map['sampleRate'] = audio.info.sample_rate
        for attr_name, tag_name in ATTR_DICT.iteritems():
            tag = audio.get(tag_name)
            attribute_value = ""
            if tag is not None:
                attribute_value = tag.text[0].encode("utf-8")

            print "Value for attribute " + attr_name + " is " + attribute_value
            feature_map[attr_name] = attribute_value



