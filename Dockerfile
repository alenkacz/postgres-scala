# This is Dockerfile that defines build environment.
FROM java:8u111-jdk-alpine
MAINTAINER varkockova.a@gmail.com

VOLUME /build
WORKDIR /build

ENTRYPOINT ["/build/gradlew"]
CMD ["test"]