#!/usr/bin/env bash

echo popup project compile and deploy...

# mvnd
mvn clean -DskipTests source:jar deploy