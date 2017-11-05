import librosa
from music_classifier import genrePrediction

def retrive_features(fileName):
    y, sr = librosa.load(fileName)
    onset_env = librosa.onset.onset_strength(y, sr=sr)
    tempo = librosa.beat.tempo(onset_envelope=onset_env, sr=sr)
    print "Tempo of music is %d" % tempo
    predictedGenres = genrePrediction(filePath=fileName)
    return {'tempo':tempo[0], 'predictedGenres': predictedGenres}