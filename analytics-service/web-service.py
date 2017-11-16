from flask import Flask
from flask import request
import soundfile as sf
from werkzeug import secure_filename
import json
from music_analyzer import retrive_features

app = Flask(__name__)

@app.route("/")
def index():
    return ('<form action="/api/upload_music" method="POST" enctype = "multipart/form-data" >'
              '<input type="file" name="music">'
              '<input type="submit">'
            '</for'
            'm>')


@app.route("/api/upload_music",  methods=['GET', 'POST'])
def upload():
  if request.method == 'POST':
    f = request.files['music']
    file_name = secure_filename(f.filename)
    f.save("store/" + file_name)
    features = retrive_features(fileName="store/" + file_name)
    return json.dumps(features), 200

if __name__ == "__main__":
    app.run(debug=True)