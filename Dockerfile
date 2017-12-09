FROM openjdk:8

COPY target/universal/scapig-api-scope-*.tgz .
COPY start-docker.sh .
RUN chmod +x start-docker.sh
RUN tar xvf scapig-api-scope-*.tgz

EXPOSE 7010