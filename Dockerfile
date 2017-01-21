# This is Dockerfile that defines build environment.
FROM java:8u111-jdk-alpine
MAINTAINER varkockova.a@gmail.com

# install bash for gradlew
RUN apk add --update \
               libstdc++ \
               bash \
 && rm /var/cache/apk/*

VOLUME /build
WORKDIR /build

ENTRYPOINT ["/build/gradlew"]
CMD ["test"]