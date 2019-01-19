# docker build -f Dockerfile -t tomas487/erp:0.1 .
FROM alpine:edge
MAINTAINER tomas487
ADD target/erp-0.0.1-SNAPSHOT.jar erp.jar
ADD tomcat.p12 tomcat.p12
EXPOSE 8443
RUN apk add --no-cache openjdk8
ENTRYPOINT ["java", "-jar", "erp.jar"]
