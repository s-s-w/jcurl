#!/usr/bin/make

HP2XX=hp2xx
HP2XX=hp2xx --mode eps
HOUSESIZE=--height 80 # Breite 80 mm
#ROCKSIZE=-t	      # Breite original (siehe ROCKSCALE)

%.pic : %.hp
	hp2xx --mode em --truesize --outfile $@ $<
%.svg : %.hp
	hp2xx --mode svg --truesize --outfile $@ $<
%.hpgl : %.hp
	hp2xx --mode hpgl --truesize --outfile $@ $<
%.eps : %.hp
	hp2xx --mode eps --truesize --outfile $@-tmp $<
	eps2eps -dLanguageLevel=1 $@-tmp $<
%.pdf : %.eps
	ps2pdf $< > $@
%.ps : %.eps
	eps2eps -dLanguageLevel=1 $< $@

IMAGES_HP=size.hp fig02.hp fig03.hp fig04a.hp fig04b.hp fig04c.hp \
		fig04d.hp fig04e.hp fig05.hp fig06.hp fig07.hp fig08.hp \
		fig09.hp fig10.hp fig11.hp fig12.hp fig13.hp fig14.hp \
		fig15.hp fig16.hp fig17.hp fig18.hp fig19.hp fig20.hp \
		fig21.hp fig22.hp fig23.hp fig24.hp fig25.hp

all: $(IMAGES_HP:%.hp=%.eps)

.PHONY: clean
clean:
	rm *.eps

size.eps:	size.hp
	$(HP2XX) --height 100 --outfile $@ $< # urspr: 100 mm hohe Zeichnung
fig02.eps:	fig02.hp
	$(HP2XX) --width 100 --outfile $@ $< # urspr: 100 mm hohe Zeichnung
fig03.eps:	fig03.hp
	$(HP2XX) --height 100 --outfile $@ $< # urspr: 100 mm hohe Zeichnung $(ROCKSIZE)
fig04a.eps:	fig04a.hp
	$(HP2XX) --height 80 --outfile $@ $< # urspr: 80 mm hohe Zeichnung
fig04b.eps:	fig04b.hp
	$(HP2XX) --height 80 --outfile $@ $< # urspr: 80 mm hohe Zeichnung
fig04c.eps:	fig04c.hp
	$(HP2XX) --height 80 --outfile $@ $< # urspr: 80 mm hohe Zeichnung
fig04d.eps:	fig04d.hp
	$(HP2XX) --height 80 --outfile $@ $< # urspr: 80 mm hohe Zeichnung
fig04e.eps:	fig04e.hp
	$(HP2XX) --height 80 --outfile $@ $< # urspr: 80 mm hohe Zeichnung
fig05.eps:	fig05.hp
	$(HP2XX) --width 120 --outfile $@ $< # urspr: 120 mm breite Zeichnung $(ROCKSIZE)
fig06.eps:	fig06.hp
	$(HP2XX) --width 60 --outfile $@ $<
fig07.eps:	fig07.hp
	$(HP2XX) --width 60 --outfile $@ $<
fig08.eps:	fig08.hp
	$(HP2XX) $(HOUSESIZE) --outfile $@ $<
fig09.eps:	fig09.hp
	$(HP2XX) $(HOUSESIZE) --outfile $@ $<
fig10.eps:	fig10.hp
	$(HP2XX) --width 60 --outfile $@ $<
fig11.eps:	fig11.hp
	$(HP2XX) --height 80 --outfile $@ $< # urspr: 80 mm hohe Zeichnung
fig12.eps:	fig12.hp
	$(HP2XX) $(HOUSESIZE) --outfile $@ $<
fig13.eps:	fig13.hp
	$(HP2XX) $(HOUSESIZE) --outfile $@ $<
fig14.eps:	fig14.hp
	$(HP2XX) --width 60 --outfile $@ $<
fig15.eps:	fig15.hp
	$(HP2XX) --height 60 --outfile $@ $< # urspr: 40 mm breite Zeichnung -t
fig16.eps:	fig16.hp
	$(HP2XX) $(HOUSESIZE) --outfile $@ $<
fig17.eps:	fig17.hp
	$(HP2XX) $(HOUSESIZE) --outfile $@ $<
fig18.eps:	fig18.hp
	$(HP2XX) --height 60 --outfile $@ $< # urspr: 60 mm breite Zeichnung -t
fig19.eps:	fig19.hp
	$(HP2XX) $(HOUSESIZE) --outfile $@ $<
fig20.eps:	fig20.hp
	$(HP2XX) $(HOUSESIZE) --outfile $@ $<
fig21.eps:	fig21.hp
	$(HP2XX) $(HOUSESIZE) --outfile $@ $<
fig22.eps:	fig22.hp
	$(HP2XX) $(HOUSESIZE) --outfile $@ $<
fig23.eps:	fig23.hp
	$(HP2XX) $(HOUSESIZE) --outfile $@ $<
fig24.eps:	fig24.hp
	$(HP2XX) $(HOUSESIZE) --outfile $@ $<
fig25.eps:	fig25.hp
	$(HP2XX) $(HOUSESIZE) --outfile $@ $<
