# Dockerfile

# jdk17 Image Start
FROM openjdk:17

# 인자 설정 - JAR_File
ARG JAR_FILE=build/libs/*.jar

# jar 파일 복제
COPY ${JAR_FILE} app.jar

# secret.properties 파일 복제 (src/main/resources 경로에 있는 파일을 복사합니다)
COPY ./src/main/resources/application-secret.properties /config/application-secret.properties

# 실행 명령어에 환경 변수 지정하여 애플리케이션이 secret.properties 파일을 사용할 수 있도록 설정합니다
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.additional-location=file:/config/application-secret.properties"]
