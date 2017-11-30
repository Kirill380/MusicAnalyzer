import time
import numpy as np
from keras import backend as K
from music_tagger_crnn import MusicTaggerCRNN
import audio_processor as ap
import sys


def sort_result(tags, preds):
    result = zip(tags, preds)
    sorted_result = sorted(result, key=lambda x: x[1], reverse=True)
    return [[name, '%5.3f' % score] for name, score in sorted_result]


def genrePrediction(filePath):

    ''' *WARNIING*
    This model use Batch Normalization, so the prediction
    is affected by batch. Use multiple, different data 
    samples together (at least 4) for reliable prediction.'''

    print('Running genrePrediction() with network: crnn and backend: %s' % (K._BACKEND))
    # setting
    audio_paths = [filePath]

    tags = ['rock', 'pop', 'alternative', 'indie', 'electronic',
            'female vocalists', 'dance', '00s', 'alternative rock', 'jazz',
            'beautiful', 'metal', 'chillout', 'male vocalists',
            'classic rock', 'soul', 'indie rock', 'Mellow', 'electronica',
            '80s', 'folk', '90s', 'chill', 'instrumental', 'punk',
            'oldies', 'blues', 'hard rock', 'ambient', 'acoustic',
            'experimental', 'female vocalist', 'guitar', 'Hip-Hop',
            '70s', 'party', 'country', 'easy listening',
            'sexy', 'catchy', 'funk', 'electro', 'heavy metal',
            'Progressive rock', '60s', 'rnb', 'indie pop',
            'sad', 'House', 'happy']

    genres = ['rock', 'pop', 'alternative', 'indie', 'electronic', 'dance', 'alternative rock', 'jazz', 'metal',
     'classic rock', 'soul', 'indie rock', 'electronica', 'folk', 'punk', 'blues', 'hard rock', 'experimental',
       'Hip-Hop', 'heavy metal', 'country', 'funk', 'electro', 'Progressive rock', 'rnb', 'indie pop', 'House']


    # prepare data like this
    melgrams = np.zeros((0, 1, 96, 1366))


    for audio_path in audio_paths:
        melgram = ap.compute_melgram(audio_path)
        melgrams = np.concatenate((melgrams, melgram), axis=0)


    model = MusicTaggerCRNN(weights='msd')

    print('Predicting...')
    start = time.time()
    pred_tags = model.predict(melgrams)

    print "Prediction is done. It took %d seconds." % (time.time()-start)

    sorted_result = sort_result(tags, pred_tags[0, :].tolist())
    print(audio_path)

    sorted_result = filter(lambda x: x[0] in genres, sorted_result)
    for item in sorted_result:
        print (item)
    print(' ')

    print 'Total = ' + str(reduce(lambda s, el: s + float(el[1]), sorted_result, 0))
    return sorted_result

if __name__ == '__main__':
    genrePrediction(sys.argv[1])
