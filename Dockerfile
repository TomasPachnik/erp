# docker build -f Dockerfile -t tomas487/erp:0.1 .
FROM openjdk:8-jre
MAINTAINER tomas487
VOLUME /log
VOLUME /certs
ADD target/erp-0.0.1-SNAPSHOT.jar erp.jar
EXPOSE 8443
ENTRYPOINT ["java", "-jar", "/erp.jar"]
