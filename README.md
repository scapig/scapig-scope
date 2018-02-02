## scapig-scope

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
