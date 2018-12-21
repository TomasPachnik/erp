FROM alpine:edge
MAINTAINER tomas487
ADD target/erp-0.0.1-SNAPSHOT.jar erp.jar
RUN apk add --no-cache openjdk8
EXPOSE 8080
ENTRYPOINT ["java", "jar", "erp.jar"]