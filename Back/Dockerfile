# Utilisation d'une image OpenJDK 17 légère
FROM openjdk:17-jdk-slim AS build

# Définition des variables d'environnement pour Maven
ENV MAVEN_VERSION=3.9.7
ENV MAVEN_HOME=/opt/maven
ENV PATH="${MAVEN_HOME}/bin:${PATH}"

# Installation des dépendances et téléchargement de Maven
RUN apt-get update && apt-get install -y wget unzip && \
    wget -q https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/${MAVEN_VERSION}/apache-maven-${MAVEN_VERSION}-bin.zip -O /tmp/maven.zip && \
    unzip /tmp/maven.zip -d /opt && \
    mv /opt/apache-maven-${MAVEN_VERSION} ${MAVEN_HOME} && \
    rm /tmp/maven.zip && \
    apt-get remove -y wget unzip && apt-get autoremove -y && rm -rf /var/lib/apt/lists/*

# Définition du répertoire de travail
WORKDIR /app

# Copie des fichiers Maven et du code source
COPY pom.xml .
COPY src ./src

# Compilation du projet avec Maven
RUN mvn clean package -DskipTests

# Utilisation d'une nouvelle image pour exécuter l'application
FROM openjdk:17-jdk-slim AS runtime

# Définition du répertoire de travail
WORKDIR /app

# Copie de l'artefact compilé depuis l'étape précédente
COPY --from=build /app/target/*.jar app.jar

# Exposition du port 8080 (modifiable selon ton application)
EXPOSE 8080

# Commande pour démarrer l'application
CMD ["java", "-jar", "app.jar"]
