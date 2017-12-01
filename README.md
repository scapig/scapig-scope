## tapi-api-scope

## Building
``
sbt clean test it:test component:test
``

## Packaging
``
sbt universal:package-zip-tarball
docker build -t tapi-api-scope .
``

## Running
``
docker run -p7010:7010 -i -a stdin -a stdout -a stderr tapi-api-scope sh start-docker.sh
``