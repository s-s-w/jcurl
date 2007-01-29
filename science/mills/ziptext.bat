@echo off
set ZIPFILE=mills.zip
echo . > c:\tmp\comment.~
echo ษอออออออออออออออออออออออออออออออออออออออออออออออออออออออออออออออออออออออออออออป >> c:\tmp\comment.~
echo บ  Ab 1998/01/01, Marcus Rohrmoser:                                           บ >> c:\tmp\comment.~
echo บ                                                                             บ >> c:\tmp\comment.~
echo บ  Ron Mills Untersuchungen ber Take-Outs '92. Acad-Zeichnungen und          บ >> c:\tmp\comment.~
echo บ  TeX-Quellen zum Text.                                                      บ >> c:\tmp\comment.~
echo บ                                                                             บ >> c:\tmp\comment.~
echo บ                                                                             บ >> c:\tmp\comment.~
echo บ                                                                             บ >> c:\tmp\comment.~
echo ศอออออออออออออออออออออออออออออออออออออออออออออออออออออออออออออออออออออออออออออผ >> c:\tmp\comment.~
echo . >> c:\tmp\comment.~
pkzip -z %ZIPFILE% < c:\tmp\comment.~ > NUL
set ZIPFILE=
