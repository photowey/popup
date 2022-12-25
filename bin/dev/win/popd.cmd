@echo off
Setlocal enabledelayedexpansion

echo cmd path: !cd!
cd !cd!

@REM mvnd
call mvn clean -DskipTests source:jar deploy