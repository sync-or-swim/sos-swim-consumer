FROM openjdk:8 AS build

WORKDIR /swim-consumer
COPY . .
RUN ./gradlew distTar

WORKDIR /distribution
RUN tar --extract --strip-components=1 --file /swim-consumer/build/distributions/swim-consumer.tar

FROM openjdk:8-alpine

WORKDIR /swim-consumer
COPY --from=build /distribution .
ENTRYPOINT ./bin/swim-consumer
