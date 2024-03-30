FROM gradle:8.7.0-jdk17-alpine as build

WORKDIR /bookstore

COPY . .

RUN gradle build


FROM amazoncorretto:17-alpine

EXPOSE 6565
COPY --from=build /bookstore/build/libs/bookstore-*.jar /bookstore.jar
ENTRYPOINT ["java", "-jar", "/bookstore.jar"]


