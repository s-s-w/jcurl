@echo off
set ZIPFILE=mills.zip
echo . > c:\tmp\comment.~
echo 浜様様様様様様様様様様様様様様様様様様様様様様様様様様様様様様様様様様様様様様� >> c:\tmp\comment.~
echo �  Ab 1998/01/01, Marcus Rohrmoser:                                           � >> c:\tmp\comment.~
echo �                                                                             � >> c:\tmp\comment.~
echo �  Ron Mills Untersuchungen �ber Take-Outs '92. Acad-Zeichnungen und          � >> c:\tmp\comment.~
echo �  TeX-Quellen zum Text.                                                      � >> c:\tmp\comment.~
echo �                                                                             � >> c:\tmp\comment.~
echo �                                                                             � >> c:\tmp\comment.~
echo �                                                                             � >> c:\tmp\comment.~
echo 藩様様様様様様様様様様様様様様様様様様様様様様様様様様様様様様様様様様様様様様� >> c:\tmp\comment.~
echo . >> c:\tmp\comment.~
pkzip -z %ZIPFILE% < c:\tmp\comment.~ > NUL
set ZIPFILE=
