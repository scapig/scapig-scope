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

## Running
``
docker run -p7010:7010 -i -a stdin -a stdout -a stderr scapig-scope sh start-docker.sh
``