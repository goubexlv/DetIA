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

# Stage 3: Install Python and setup virtual environment
FROM python:3.12-slim AS python
WORKDIR /app

# Copier le fichier requirements.txt ou créer un fichier pour les dépendances Python
COPY requirements.txt /app/requirements.txt

# Créer un environnement virtuel et installer les dépendances
RUN python3 -m venv /app/venv
RUN /app/venv/bin/pip install -r /app/requirements.txt

# Stage 4: Create the Runtime Image
FROM amazoncorretto:22 AS runtime
EXPOSE 8080
RUN mkdir /app

# Copier l'artefact jar généré par Gradle
COPY --from=build /home/gradle/src/build/libs/*.jar /app/Detia.jar

# Copier le répertoire Python et l'environnement virtuel
COPY --from=python /app/venv /app/venv

# Copier le script Python (ex: analyze.py)
COPY scrypt/analyze.py /app/analyze.py
COPY scrypt/analyzetext.py /app/analyzetext.py
COPY scrypt/telemodel.py /app/telemodel.py

RUN /app/venv/bin/python3 /app/telemodel.py

# Ajouter le script d'entrée
COPY entrypoint.sh /app/entrypoint.sh
RUN chmod +x /app/entrypoint.sh

# Lancer le script Python puis Ktor
ENTRYPOINT ["java","-jar","/app/Detia.jar"]

