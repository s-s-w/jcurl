#!/usr/bin/make

HP2XX=hp2xx
HP2XX=hp2xx -m eps
HOUSESIZE=	-w 80	      # Breite 80 mm
ROCKSIZE=	-t	      # Breite original (siehe ROCKSCALE)

%.pic : %.hp
	hp2xx -m em -t -f $@ $<
%.eps : %.hp
	hp2xx -m eps -t -f $@ $<
%.ps : %.eps
	ps2ps $< $@

VERSUCH_EPS=	size.eps fig02.eps fig03.eps fig04a.eps fig04b.eps fig04c.eps \
		fig04d.eps fig04e.eps fig05.eps fig06.eps fig07.eps fig08.eps \
		fig09.eps fig10.eps fig11.eps fig12.eps fig13.eps fig14.eps \
		fig15.eps fig16.eps fig17.eps fig18.eps fig19.eps fig20.eps \
		fig21.eps fig22.eps fig23.eps fig24.eps fig25.eps

all: $(VERSUCH_EPS)


.PHONY: clean
clean:
	rm *.eps *.svg *.aux *.toc *.lof


size.eps:	size.hp
	$(HP2XX) -h100 -f $@ $< # urspr: 100 mm hohe Zeichnung
fig02.eps:	fig02.hp
	$(HP2XX) $(ROCKSIZE) -f $@ $< # urspr: 100 mm hohe Zeichnung
fig03.eps:	fig03.hp
	$(HP2XX) $(ROCKSIZE) -f $@ $< # urspr: 100 mm hohe Zeichnung
fig04a.eps:	fig04a.hp
	$(HP2XX) -h 80 -f $@ $< # urspr: 80 mm hohe Zeichnung
fig04b.eps:	fig04b.hp
	$(HP2XX) -h 80 -f $@ $< # urspr: 80 mm hohe Zeichnung
fig04c.eps:	fig04c.hp
	$(HP2XX) -h 80 -f $@ $< # urspr: 80 mm hohe Zeichnung
fig04d.eps:	fig04d.hp
	$(HP2XX) -h 80 -f $@ $< # urspr: 80 mm hohe Zeichnung
fig04e.eps:	fig04e.hp
	$(HP2XX) -h 80 -f $@ $< # urspr: 80 mm hohe Zeichnung
fig05.eps:	fig05.hp
	$(HP2XX) $(ROCKSIZE) -f $@ $< # urspr: 120 mm breite Zeichnung
fig06.eps:	fig06.hp
	$(HP2XX) $(HOUSESIZE) -f $@ $<
fig07.eps:	fig07.hp
	$(HP2XX) $(HOUSESIZE) -f $@ $<
fig08.eps:	fig08.hp
	$(HP2XX) $(HOUSESIZE) -f $@ $<
fig09.eps:	fig09.hp
	$(HP2XX) $(HOUSESIZE) -f $@ $<
fig10.eps:	fig10.hp
	$(HP2XX) $(HOUSESIZE) -f $@ $<
fig11.eps:	fig11.hp
	$(HP2XX) -h 80 -f $@ $< # urspr: 80 mm hohe Zeichnung
fig12.eps:	fig12.hp
	$(HP2XX) $(HOUSESIZE) -f $@ $<
fig13.eps:	fig13.hp
	$(HP2XX) $(HOUSESIZE) -f $@ $<
fig14.eps:	fig14.hp
	$(HP2XX) $(HOUSESIZE) -f $@ $<
fig15.eps:	fig15.hp
	$(HP2XX) -t -f $@ $< # urspr: 40 mm breite Zeichnung
fig16.eps:	fig16.hp
	$(HP2XX) $(HOUSESIZE) -f $@ $<
fig17.eps:	fig17.hp
	$(HP2XX) $(HOUSESIZE) -f $@ $<
fig18.eps:	fig18.hp
	$(HP2XX) -t -f $@ $< # urspr: 60 mm breite Zeichnung
fig19.eps:	fig19.hp
	$(HP2XX) $(HOUSESIZE) -f $@ $<
fig20.eps:	fig20.hp
	$(HP2XX) $(HOUSESIZE) -f $@ $<
fig21.eps:	fig21.hp
	$(HP2XX) $(HOUSESIZE) -f $@ $<
fig22.eps:	fig22.hp
	$(HP2XX) $(HOUSESIZE) -f $@ $<
fig23.eps:	fig23.hp
	$(HP2XX) $(HOUSESIZE) -f $@ $<
fig24.eps:	fig24.hp
	$(HP2XX) $(HOUSESIZE) -f $@ $<
fig25.eps:	fig25.hp
	$(HP2XX) $(HOUSESIZE) -f $@ $<
