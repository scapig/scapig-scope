FROM openjdk:8

COPY target/universal/tapi-api-scope-*.tgz .
COPY start-docker.sh .
RUN chmod +x start-docker.sh
RUN tar xvf tapi-api-scope-*.tgz

EXPOSE 7010