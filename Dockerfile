# docker build -f Dockerfile -t tomas487/erp:0.1 .
FROM openjdk:8-jdk-alpine
MAINTAINER tomas487
VOLUME /tmp
COPY target/erp-0.0.1-SNAPSHOT.jar erp.jar
COPY tomcat.p12 tomcat.p12
EXPOSE 8443
RUN apk add --no-cache openjdk8
ENTRYPOINT ["java", "-jar", "/erp.jar"]
