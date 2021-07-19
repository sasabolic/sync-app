FROM openjdk:15-jdk-alpine

ENV VRS_POSTGRES db

EXPOSE 8080

ADD ./build/libs/*.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
