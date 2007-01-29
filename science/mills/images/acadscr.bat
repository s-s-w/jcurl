@echo off
@rem echo Parameter 1: Dateiname (Input)   : %1
@rem echo Parameter 2: Ausschnitt	   : %2
@rem echo Parameter 3: Ausschnitt (Output) : %3
@rem echo Parameter 4: scale mm=units mm   : %4
@rem echo Parameter 4: scale mm=units units: %5
echo BEGIN{print "3 %1 a %2 j n j m   n 0.01 n n %4=%5 %3 ";} > plot.awk
awk -f plot.awk >> plot.scr
del plot.awk
