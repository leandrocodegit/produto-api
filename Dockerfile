FROM openjdk:11
EXPOSE 8080:9092
ADD /build/libs/produto-0.0.1-SNAPSHOT.jar produto-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "produto-0.0.1-SNAPSHOT.jar"]