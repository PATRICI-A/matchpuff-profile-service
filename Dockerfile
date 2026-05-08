# ── Stage 1: Build ─────────────────────────────────────────
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src

ARG SSL_KEY_STORE_PASSWORD
RUN keytool -genkeypair \
    -alias profile-service \
    -keyalg RSA \
    -keysize 2048 \
    -storetype PKCS12 \
    -keystore src/main/resources/keystore.p12 \
    -validity 3650 \
    -storepass "${SSL_KEY_STORE_PASSWORD}" \
    -keypass "${SSL_KEY_STORE_PASSWORD}" \
    -dname "CN=profile-service,OU=MatchPuff,O=MatchPuff,L=Unknown,S=Unknown,C=CO" \
    -noprompt

RUN mvn package -DskipTests -B

# ── Stage 2: Runtime ───────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# instalar wget para healthcheck
RUN apk add --no-cache wget

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8443

HEALTHCHECK --interval=10s --timeout=3s --retries=3 \
  CMD wget -qO- --no-check-certificate https://localhost:8443/actuator/health | grep UP || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
