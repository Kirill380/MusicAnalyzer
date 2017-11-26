#!/usr/bin/env bash

if ! type python2; then
  echo 'Installing home brew python'
  brew install python
fi


if ! type virtualenv; then
  echo 'Installing virtualenv'
  pip install virtualenv
fi

virtualenv analytic_service

source analytic_service/bin/activate
pip install keras==1.1.0 h5py theano==0.8.2 librosa Flask pydub pysoundfile mutagen Pillow matplotlib

if [ ! -e ~/.matplotlib/matplotlibrc ]; then
    echo "backend: TkAgg" >> ~/.matplotlib/matplotlibrc
fi

python2 web-service.py