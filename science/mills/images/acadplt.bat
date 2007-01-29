@echo off
rem echo Parameter 1: Dateiname (Input)   : %1
rem echo Parameter 2: Ausschnitt	  : %2
rem echo Parameter 3: Ausschnitt (Output) : %3
rem echo Parameter 4: scale mm		  : %4
rem echo Parameter 4: scale units	  : %5
if exist plot.scr del plot.scr
call acadscr %1 %2 %3 %4 %5
echo 0 >> plot.scr
call acad dummy plot.scr
del plot.scr
