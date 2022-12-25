@echo off

echo popup project compile and deploy...

@REM call mvn clean -DskipTests source:jar deploy -pl ^
@REM popup-bom ^
@REM -am %*

@REM call mvnd
call mvn clean -DskipTests source:jar deploy

@REM exit