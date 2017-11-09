FROM python:3

COPY analytics-service/ /

RUN pip install keras==1.1.0 h5py theano==0.8.2 librosa Flask pydub pysoundfile mutagen

CMD [ "python", "./web-service.py" ]