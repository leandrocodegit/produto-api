FROM openjdk:11
LABEL maintainer="VITRINE"
EXPOSE  9092
VOLUME imagens-produto:/workspace/imagens
ADD /build/libs/produto-0.0.1-SNAPSHOT.jar produto-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "produto-0.0.1-SNAPSHOT.jar"]