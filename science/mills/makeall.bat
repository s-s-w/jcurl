path > opath.bat
rem set PATH=c:\bin;c:\usr\bin;c:\usr\local\bin;d:\marcus\bin\dos;c:\usr\texmf\bin;c:\bin\emx.09a\bin
call acadplt fig02 fig02 fig02.hp 1750 29
call acadplt fig03 fig03 fig03.hp 1750 29
call acadplt fig04 fig04a fig04a.hp 20 1
call acadplt fig04 fig04b fig04b.hp 20 1
call acadplt fig04 fig04c fig04c.hp 20 1
call acadplt fig04 fig04d fig04d.hp 20 1
call acadplt fig04 fig04e fig04e.hp 20 1
call acadplt fig05 fig05 fig05.hp 1750 29
call acadplt fig06 fig06 fig06.hp 8000 497
call acadplt fig07 fig07 fig07.hp 8000 497
call acadplt fig08 fig08 fig08.hp 8000 497
call acadplt fig09 fig09 fig09.hp 8000 497
call acadplt fig10 fig10 fig10.hp 8000 497
call acadplt fig11 fig11 fig11.hp 1750 29
call acadplt fig12 fig12 fig12.hp 8000 497
call acadplt fig13 fig13 fig13.hp 8000 497
call acadplt fig14 fig14 fig14.hp 8000 497
call acadplt fig15 fig15 fig15.hp 4000 29
call acadplt fig16 fig16 fig16.hp 8000 497
call acadplt fig17 fig17 fig17.hp 8000 497
call acadplt fig18 fig18 fig18.hp 1750 29
call acadplt fig19 fig19 fig19.hp 8000 497
call acadplt fig20 fig20 fig20.hp 8000 497
call acadplt fig21 fig21 fig21.hp 8000 497
call acadplt fig22 fig22 fig22.hp 8000 497
call acadplt fig23 fig23 fig23.hp 8000 497
call acadplt fig24 fig24 fig24.hp 8000 497
call acadplt fig25 fig25 fig25.hp 8000 497
call acadplt size2 size size.hp 8000 497
call hp2xx -m em -h 80 -f fig04a.pic fig04a.hp
call hp2xx -m em -h 80 -f fig04b.pic fig04b.hp
call hp2xx -m em -h 80 -f fig04c.pic fig04c.hp
call hp2xx -m em -h 80 -f fig04d.pic fig04d.hp
call hp2xx -m em -h 80 -f fig04e.pic fig04e.hp
call hp2xx -m em -h 80 -f fig11.pic fig11.hp
call hp2xx -m em -h100 -f size.pic size.hp
call hp2xx -m em -t -f fig02.pic fig02.hp
call hp2xx -m em -t -f fig03.pic fig03.hp
call hp2xx -m em -t -f fig05.pic fig05.hp
call hp2xx -m em -t -f fig06.pic fig06.hp
call hp2xx -m em -t -f fig07.pic fig07.hp
call hp2xx -m em -t -f fig08.pic fig08.hp
call hp2xx -m em -t -f fig09.pic fig09.hp
call hp2xx -m em -t -f fig10.pic fig10.hp
call hp2xx -m em -t -f fig12.pic fig12.hp
call hp2xx -m em -t -f fig13.pic fig13.hp
call hp2xx -m em -t -f fig14.pic fig14.hp
call hp2xx -m em -t -f fig15.pic fig15.hp
call hp2xx -m em -t -f fig16.pic fig16.hp
call hp2xx -m em -t -f fig17.pic fig17.hp
call hp2xx -m em -t -f fig18.pic fig18.hp
call hp2xx -m em -t -f fig19.pic fig19.hp
call hp2xx -m em -t -f fig20.pic fig20.hp
call hp2xx -m em -t -f fig21.pic fig21.hp
call hp2xx -m em -t -f fig22.pic fig22.hp
call hp2xx -m em -t -f fig23.pic fig23.hp
call hp2xx -m em -t -f fig24.pic fig24.hp
call hp2xx -m em -t -f fig25.pic fig25.hp
call opath.bat
del  opath.bat
call latex mills
call latex mills
call latex mills
call dviwiew mills /fa-
