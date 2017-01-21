# This is Dockerfile that defines build environment.
FROM java:8u111-jdk-alpine
MAINTAINER varkockova.a@gmail.com

# to avoid libnative-platform.so missing bug in gradle
RUN apk add --update \
               libstdc++ \
 && rm /var/cache/apk/*

VOLUME /build
WORKDIR /build

ENTRYPOINT ["/build/gradlew"]
CMD ["test"]