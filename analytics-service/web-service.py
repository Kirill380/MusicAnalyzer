from flask import Flask
from flask import Response

import json
from music_analyzer import retrieve_features

app = Flask(__name__)


@app.route("/api/v1/analyzer/musics/<id>/features",  methods=['POST'])
def calculate_music_features(id):
    print "Calculating features for music " + id
    features = retrieve_features(id)
    return Response(response=json.dumps(features), status=200, mimetype="application/json")

if __name__ == "__main__":
    app.run(port = 8084, debug=True)