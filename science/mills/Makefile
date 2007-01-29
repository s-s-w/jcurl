#!/usr/bin/make

mills.pdf: mills.tex
	make -C images
	cp images/*.eps .
	latex mills
	latex mills
	latex mills
	dvipdfm mills

.PHONY: clean
clean:
	make -C images clean
	rm *.eps *.aux *.toc *.lof *.dvi
