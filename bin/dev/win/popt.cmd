@echo off
setlocal EnableDelayedExpansion

cd !cd!
echo cmd path: !cd!

@REM mvnd
call mvnd dependency:tree -Dincludes=:%1