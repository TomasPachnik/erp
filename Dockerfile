# docker build -f Dockerfile -t tomas487/erp-arm:0.1 .
FROM openjdk:8-jre-alpine
MAINTAINER tomas487
VOLUME /log
VOLUME /certs
ADD target/erp-0.0.1-SNAPSHOT.jar erp.jar
EXPOSE 8442
ENTRYPOINT ["java", "-jar", "/erp.jar"]
