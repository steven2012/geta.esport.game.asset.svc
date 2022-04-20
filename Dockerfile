# Alpine for core-service-1.0-local-SNAPSHOT
# ./gradlew clean build
# Build image with:  docker build -t genesycontainersport.azurecr.io/geta-sport-games.assets .
# RUN CONTAINER: docker run -d -p 8082:9000 genesycontainersport.azurecr.io/geta-sport-games.assets

# Use a parent image
FROM openjdk:8-jdk-slim
#FROM 10.25.10.193:8090/centos8_jdk8:v1
WORKDIR /opt/geta/games/services/geta-sport-games.assets
ADD build/libs /opt/geta/games/services/geta-sport-games.assets/
ADD src/main/resources  /opt/geta/games/services/geta-sport-games.assets/resources/

### Set Environment
#ENV SERVER_HOME /opt/geta/games/services/geta-sport-games.assets

# Make internal port available to the world outside this container
EXPOSE 9000

#permisos de carpeta
RUN chmod -R 777 /opt/geta

ENV TZ=America/Bogota
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

ENV JAVA_OPTS="-Xmx1g"

### Start instance
ENTRYPOINT ["java","-jar", "-Dspring.profiles.active=dev", "-Dfile.encoding=ISO8859_1", "/opt/geta/games/services/geta-sport-games.assets/geta.esports.game.assets.svc-1.0-SNAPSHOT.jar"]

# Run app when the container launches
#CMD ["java", "-jar", "geta.esports.game.assets.svc-1.0-SNAPSHOT.jar"]