LATEX=		latex
PREVIEW=	dviview
DELCMD= 	del
AWK=		awk
HP2XX=		hp2xx -m em
DEF_SCALE=	20 1	      # 50mm (paper) is 1 acad-unit (=meter)
ROCKSCALE=	1750 29       # Breite 17.5mm = 0.29 Meter
LARGEROCKSCALE= 4000 29       #
HOUSESCALE=	8000 497      # Breite 80mm = 4.97 Meter (Breite Rink)
HOUSESIZE=	-w 80	      # Breite 80 mm
ROCKSIZE=	-t	      # Breite original (siehe ROCKSCALE)
HOUSESIZE=	-t	      # Breite original (siehe HOUSESCALE)
CFLAGS=
GNUTERM=	terminal.gpt
VERSUCH=	mills
VERSUCH_GRA=
VERSUCH_PIC=	size.pic fig02.pic fig03.pic fig04a.pic fig04b.pic fig04c.pic \
		fig04d.pic fig04e.pic fig05.pic fig06.pic fig07.pic fig08.pic \
		fig09.pic fig10.pic fig11.pic fig12.pic fig13.pic fig14.pic \
		fig15.pic fig16.pic fig17.pic fig18.pic fig19.pic fig20.pic \
		fig21.pic fig22.pic fig23.pic fig24.pic fig25.pic
VERSUCH_SRC=	$(VERSUCH_GRA) $(VERSUCH_PIC) $(VERSUCH).tex

all:	$(VERSUCH).dvi

2unix:
	trans -lisbnw8 makefile *.dat *.awk *.tex *.gpt *.pas
2dos:
	trans -cxsbnw8 makefile *.dat *.awk *.tex *.gpt *.pas

full:
	make all
	$(LATEX) $(VERSUCH)

clean:
	$(DELCMD) *.aux
	$(DELCMD) *.lof
	$(DELCMD) *.toc
	$(DELCMD) *.bak
	$(DELCMD) *.gra
	$(DELCMD) *.log
	$(DELCMD) *.hp
	$(DELCMD) *.pic
	$(DELCMD) *.~*

soft:
	attrib -a *.bak
	attrib -a *.gra
	attrib -a *.log
	attrib -a *.pic
	attrib -a *.~*

view:
	$(PREVIEW) $(VERSUCH)

$(VERSUCH).dvi: $(VERSUCH_SRC) $(GNUTERM) makefile
	$(LATEX) $(VERSUCH)

size.pic:	size.hp
	$(HP2XX) -h100 -f $*.pic $*.hp # urspr: 100 mm hohe Zeichnung
fig02.pic:	fig02.hp
	$(HP2XX) $(ROCKSIZE) -f $*.pic $*.hp # urspr: 100 mm hohe Zeichnung
fig03.pic:	fig03.hp
	$(HP2XX) $(ROCKSIZE) -f $*.pic $*.hp # urspr: 100 mm hohe Zeichnung
fig04a.pic:	fig04a.hp
	$(HP2XX) -h 80 -f $*.pic $*.hp # urspr: 80 mm hohe Zeichnung
fig04b.pic:	fig04b.hp
	$(HP2XX) -h 80 -f $*.pic $*.hp # urspr: 80 mm hohe Zeichnung
fig04c.pic:	fig04c.hp
	$(HP2XX) -h 80 -f $*.pic $*.hp # urspr: 80 mm hohe Zeichnung
fig04d.pic:	fig04d.hp
	$(HP2XX) -h 80 -f $*.pic $*.hp # urspr: 80 mm hohe Zeichnung
fig04e.pic:	fig04e.hp
	$(HP2XX) -h 80 -f $*.pic $*.hp # urspr: 80 mm hohe Zeichnung
fig05.pic:	fig05.hp
	$(HP2XX) $(ROCKSIZE) -f $*.pic $*.hp # urspr: 120 mm breite Zeichnung
fig06.pic:	fig06.hp
	$(HP2XX) $(HOUSESIZE) -f $*.pic $*.hp
fig07.pic:	fig07.hp
	$(HP2XX) $(HOUSESIZE) -f $*.pic $*.hp
fig08.pic:	fig08.hp
	$(HP2XX) $(HOUSESIZE) -f $*.pic $*.hp
fig09.pic:	fig09.hp
	$(HP2XX) $(HOUSESIZE) -f $*.pic $*.hp
fig10.pic:	fig10.hp
	$(HP2XX) $(HOUSESIZE) -f $*.pic $*.hp
fig11.pic:	fig11.hp
	$(HP2XX) -h 80 -f $*.pic $*.hp # urspr: 80 mm hohe Zeichnung
fig12.pic:	fig12.hp
	$(HP2XX) $(HOUSESIZE) -f $*.pic $*.hp
fig13.pic:	fig13.hp
	$(HP2XX) $(HOUSESIZE) -f $*.pic $*.hp
fig14.pic:	fig14.hp
	$(HP2XX) $(HOUSESIZE) -f $*.pic $*.hp
fig15.pic:	fig15.hp
	$(HP2XX) -t -f $*.pic $*.hp # urspr: 40 mm breite Zeichnung
fig16.pic:	fig16.hp
	$(HP2XX) $(HOUSESIZE) -f $*.pic $*.hp
fig17.pic:	fig17.hp
	$(HP2XX) $(HOUSESIZE) -f $*.pic $*.hp
fig18.pic:	fig18.hp
	$(HP2XX) -t -f $*.pic $*.hp # urspr: 60 mm breite Zeichnung
fig19.pic:	fig19.hp
	$(HP2XX) $(HOUSESIZE) -f $*.pic $*.hp
fig20.pic:	fig20.hp
	$(HP2XX) $(HOUSESIZE) -f $*.pic $*.hp
fig21.pic:	fig21.hp
	$(HP2XX) $(HOUSESIZE) -f $*.pic $*.hp
fig22.pic:	fig22.hp
	$(HP2XX) $(HOUSESIZE) -f $*.pic $*.hp
fig23.pic:	fig23.hp
	$(HP2XX) $(HOUSESIZE) -f $*.pic $*.hp
fig24.pic:	fig24.hp
	$(HP2XX) $(HOUSESIZE) -f $*.pic $*.hp
fig25.pic:	fig25.hp
	$(HP2XX) $(HOUSESIZE) -f $*.pic $*.hp

size.hp:       size2.dwg
	acadplt size2 size size.hp $(HOUSESCALE)
fig02.hp:      fig02.dwg
	acadplt $* $* $*.hp $(ROCKSCALE)
fig03.hp:      fig03.dwg
	acadplt $* $* $*.hp $(ROCKSCALE)
fig04a.hp:     fig04.dwg
	acadplt fig04 fig04a fig04a.hp $(DEF_SCALE)
fig04b.hp:     fig04.dwg
	acadplt fig04 fig04b fig04b.hp $(DEF_SCALE)
fig04c.hp:     fig04.dwg
	acadplt fig04 fig04c fig04c.hp $(DEF_SCALE)
fig04d.hp:     fig04.dwg
	acadplt fig04 fig04d fig04d.hp $(DEF_SCALE)
fig04e.hp:     fig04.dwg
	acadplt fig04 fig04e fig04e.hp $(DEF_SCALE)
fig05.hp:      fig05.dwg
	acadplt $* $* $*.hp $(ROCKSCALE)
fig06.hp:      fig06.dwg
	acadplt $* $* $*.hp $(HOUSESCALE)
fig07.hp:      fig07.dwg
	acadplt $* $* $*.hp $(HOUSESCALE)
fig08.hp:      fig08.dwg
	acadplt $* $* $*.hp $(HOUSESCALE)
fig09.hp:      fig09.dwg
	acadplt $* $* $*.hp $(HOUSESCALE)
fig10.hp:      fig10.dwg
	acadplt $* $* $*.hp $(HOUSESCALE)
fig11.hp:      fig11.dwg
	acadplt $* $* $*.hp $(ROCKSCALE)
fig12.hp:      fig12.dwg
	acadplt $* $* $*.hp $(HOUSESCALE)
fig13.hp:      fig13.dwg
	acadplt $* $* $*.hp $(HOUSESCALE)
fig14.hp:      fig14.dwg
	acadplt $* $* $*.hp $(HOUSESCALE)
fig15.hp:      fig15.dwg
	acadplt $* $* $*.hp $(LARGEROCKSCALE)
fig16.hp:      fig16.dwg
	acadplt $* $* $*.hp $(HOUSESCALE)
fig17.hp:      fig17.dwg
	acadplt $* $* $*.hp $(HOUSESCALE)
fig18.hp:      fig18.dwg
	acadplt $* $* $*.hp $(ROCKSCALE)
fig19.hp:      fig19.dwg
	acadplt $* $* $*.hp $(HOUSESCALE)
fig20.hp:      fig20.dwg
	acadplt $* $* $*.hp $(HOUSESCALE)
fig21.hp:      fig21.dwg
	acadplt $* $* $*.hp $(HOUSESCALE)
fig22.hp:      fig22.dwg
	acadplt $* $* $*.hp $(HOUSESCALE)
fig23.hp:      fig23.dwg
	acadplt $* $* $*.hp $(HOUSESCALE)
fig24.hp:      fig24.dwg
	acadplt $* $* $*.hp $(HOUSESCALE)
fig25.hp:      fig25.dwg
	acadplt $* $* $*.hp $(HOUSESCALE)

