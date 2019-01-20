# docker build -f Dockerfile -t tomas487/erp:0.1 .
FROM openjdk:8-jre
MAINTAINER tomas487
VOLUME /log
ADD target/erp-0.0.1-SNAPSHOT.jar erp.jar
ADD tomcat.p12 tomcat.p12
EXPOSE 8443
ENTRYPOINT ["java", "-jar", "/erp.jar"]
