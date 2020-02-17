FROM gradle:6.1.1-jdk13 AS build

WORKDIR /sos-swim-consumer
COPY . .
RUN gradle distTar

WORKDIR /distribution
RUN tar --extract --strip-components=1 --file /sos-swim-consumer/build/distributions/sos-swim-consumer.tar

FROM openjdk:8-alpine

WORKDIR /sos-swim-consumer
COPY --from=build /distribution .
ENTRYPOINT ["./bin/sos-swim-consumer"]

