FROM openjdk:17-jdk

ADD build/libs/CopywriterGPT*.jar cwgpt.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Xmx2048m", "-Xms2048m", "-jar", "cwgpt.jar", "--spring.profiles.active=${SPRING_PROFILE}"]

HEALTHCHECK --start-period=30s --interval=10s CMD curl -f http://localhost:8080/actuator/health || exit 1