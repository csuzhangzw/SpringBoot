FROM openjdk:8-jre
MAINTAINER dylan csuzhangzw@outlook.com

COPY target/user-service-0.0.1-SNAPSHOT.jar /user-service.jar

EXPOSE 8081

ENTRYPOINT ["export", "zookeeper_host=10.211.55.29"]

ENTRYPOINT ["java", "-jar", "/user-service.jar"]