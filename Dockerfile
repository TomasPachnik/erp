FROM alpine:edge
MAINTAINER tomas487
RUN apk add --no-cache openjdk8
COPY files/UnlimitedJCEPolicyJDK8/* \
  /usr/lib/jvm/java-1.8-openjdk/jre/lib/security/