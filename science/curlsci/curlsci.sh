#!/bin/sh

# hp2xx -m em -h 50 distance.hp
# gnuplot   denny.gpt
# gnuplot   contact.gpt
pdflatex	curlsci
bibtex		curlsci
pdflatex	curlsci
makeindex -lc curlsci
pdflatex	curlsci
