FROM openjdk:8-jre
MAINTAINER dylan csuzhangzw@outlook.com

COPY target/elasticsearch-0.0.1-SNAPSHOT.jar /elasticsearch.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/elasticsearch.jar"]