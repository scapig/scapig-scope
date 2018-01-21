FROM openjdk:8

COPY target/universal/scapig-scope-*.tgz .
COPY start-docker.sh .
RUN chmod +x start-docker.sh
RUN tar xvf scapig-scope-*.tgz
EXPOSE 9011

CMD ["sh", "start-docker.sh"]
