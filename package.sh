#!/bin/sh
sbt universal:package-zip-tarball
docker build -t scapig-scope .
docker tag scapig-scope scapig/scapig-scope
docker push scapig/scapig-scope
