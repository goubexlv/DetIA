# Stage 1: Cache Gradle dependencies
FROM gradle:8.4-jdk17 AS cache
RUN mkdir -p /home/gradle/cache_home
ENV GRADLE_USER_HOME=/home/gradle/cache_home
COPY build.gradle.* gradle.properties /home/gradle/app/
COPY gradle /home/gradle/app/gradle
WORKDIR /home/gradle/app
RUN gradle clean build -i --stacktrace

# Stage 2: Build Application
FROM gradle:8.4-jdk17 AS build
COPY --from=cache /home/gradle/cache_home /home/gradle/.gradle
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle buildFatJar --no-daemon

# Stage 3: Final runtime image with Python and Java
FROM amazoncorretto:22 AS runtime
EXPOSE 8080
RUN mkdir /app

# Installer Python et pip
RUN apt -y update && \
    apt -y install python3 && \
    pip3 install --upgrade pip

# Copier le JAR
COPY --from=build /home/gradle/src/build/libs/*.jar /app/Detia.jar

# Copier les fichiers Python
COPY requirements.txt /app/requirements.txt
COPY scrypt/*.py /app/

# Installer les dépendances Python
RUN pip3 install -r /app/requirements.txt

# Ajouter le script d'entrée
COPY entrypoint.sh /app/entrypoint.sh
RUN chmod +x /app/entrypoint.sh

# Entrée principale
ENTRYPOINT ["/app/entrypoint.sh"]
