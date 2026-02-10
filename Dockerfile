#FROM maven:3.8.3-openjdk-17 AS build
#COPY . .
#RUN mvn clean install
#
##
## Package stage
##
#FROM eclipse-temurin:21-jdk
#COPY --from=build /target/saloon_app-0.0.1-SNAPSHOT.jar demo.jar
## ENV PORT=8080
#EXPOSE 8080
#ENTRYPOINT ["java","-jar","demo.jar"]



# ---------- BUILD STAGE ----------
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

# ---------- RUN STAGE ----------
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /app/target/saloon_app-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]

