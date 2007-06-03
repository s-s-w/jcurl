#!/usr/bin/make

HP2XX=hp2xx
HP2XX=hp2xx --mode eps
HOUSESIZE=--height 80 # Breite 80 mm
#ROCKSIZE=-t	      # Breite original (siehe ROCKSCALE)

#%.eps : %.dxf
#	# requires e.g. Kabeja 0.3+
#	inkscape --export-eps=$@ $<
#%.pic : %.hp
#	hp2xx --mode em --truesize --outfile $@ $<
#%.svg : %.hp
#	hp2xx --mode svg --truesize --outfile $@ $<
#%.hpgl : %.hp
#	hp2xx --mode hpgl --truesize --outfile $@ $<
%.eps : %.hp
	hp2xx --mode eps --truesize --outfile $@ $<
#	hp2xx --mode eps --truesize --outfile $@-tmp $<
#	eps2eps -dLanguageLevel=1 $@-tmp $@
#	rm $@-tmp
#%.pdf : %.eps
#	ps2pdf $< > $@
#%.ps : %.eps
#	eps2eps -dLanguageLevel=1 $< $@

IMAGES_HP=size.hp fig02.hp fig03.hp fig04a.hp fig04b.hp fig04c.hp \
		fig04d.hp fig04e.hp fig05.hp fig06.hp fig07.hp fig08.hp \
		fig09.hp fig10.hp fig11.hp fig12.hp fig13.hp fig14.hp \
		fig15.hp fig16.hp fig17.hp fig18.hp fig19.hp fig20.hp \
		fig21.hp fig22.hp fig23.hp fig24.hp fig25.hp

all: $(IMAGES_HP:%.hp=%.eps)

.PHONY: clean
clean:
	rm *.eps
