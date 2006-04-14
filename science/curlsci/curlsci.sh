#!/bin/sh

hp2xx -m em -h 50 distance.hp
gnuplot   denny.gpt
gnuplot   contact.gpt
latex     curlsci
bibtex    curlsci
latex     curlsci
makeindex -lc curlsci
latex     curlsci
