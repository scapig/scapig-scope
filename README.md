## scapig-scope

This is the microservice which stores and retrieves the scope defined for the authorization as part of the Oauth 2.0 specs.
It is part of the Scapig API Manager (http://scapig.com)

## Building
``
sbt clean test it:test component:test
``

## Packaging
``
sbt universal:package-zip-tarball
docker build -t scapig-scope .
``

## Publishing
``
docker tag scapig-scope scapig/scapig-scope
docker login
docker push scapig/scapig-scope
``

## Running
``
docker run -p9011:9011 -d scapig/scapig-scope
``
