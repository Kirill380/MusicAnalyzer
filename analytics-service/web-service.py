from flask import Flask
from flask import Response
from flask import request

import json
from music_analyzer import retrieve_features
from music_analyzer import get_time_series
from music_analyzer import get_spectrum

app = Flask(__name__)


@app.route("/api/v1/analyzer/musics/<id>/features",  methods=['POST'])
def calculate_music_features(id):
    print "Calculating features for music " + id
    features = retrieve_features(id)
    return Response(response=json.dumps(features), status=200, mimetype="application/json")


@app.route("/api/v1/analyzer/musics/<id>/time-series",  methods=['GET'])
def get_music_time_series(id):
    from_sec = request.args.get('from')
    to_sec = request.args.get('to')
    time_series = get_time_series(id, int(from_sec), int(to_sec))
    return Response(response=json.dumps(time_series), status=200, mimetype="application/json")


@app.route("/api/v1/analyzer/musics/<id>/spectrogram",  methods=['GET'])
def get_music_spectrum(id):
    from_sec = request.args.get('from')
    to_sec = request.args.get('to')
    spectrum = get_spectrum(id, int(from_sec), int(to_sec))
    return Response(response=json.dumps(spectrum), status=200, mimetype="application/json")



if __name__ == "__main__":
    app.run(port = 8084, debug=True)