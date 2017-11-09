FROM openjdk:8-jre

COPY docker/musci-analyzer-service/musci-analyzer-conf/ /musci-analyzer-conf

RUN find /musci-analyzer-conf -type f -exec chmod 777 {} \;

ARG setupfile
COPY ["$setupfile", "/musci-analyzer-service.jar"]

EXPOSE 30750

ENTRYPOINT ["/musci-analyzer-conf/docker-entry.sh"]
