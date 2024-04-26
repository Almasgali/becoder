# syntax=docker/dockerfile:1
FROM gradle:8.7.0-jdk21-jammy as build

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon -x test

RUN mkdir -p /home/gradle/target/dependency \
&& (cd /home/gradle/target/dependency; for i in ../../src/build/libs/*.jar; do jar -xf $i; done)

FROM eclipse-temurin:21-jdk-alpine
RUN addgroup -S demo && adduser -S demo -G demo
USER demo
VOLUME /tmp
ARG DEPENDENCY=/home/gradle/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes/liquibase /app/liquibase
ENTRYPOINT ["java","-cp","app:app/lib/*","ru.becoder.krax.KraxApplication"]