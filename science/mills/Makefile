#!/usr/bin/make

mills.pdf: mills.tex
	make -C images
	cp images/*.pdf .
	pdflatex mills
	pdflatex mills
	pdflatex mills
#	dvipdfm mills

.PHONY: clean
clean:
	make -C images clean
	rm *.eps *.aux *.toc *.lof *.dvi
