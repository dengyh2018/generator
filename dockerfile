FROM jdk:8
VOLUME /tmp
ADD target/generator.jar app.jar
EXPOSE 8888
ENTRYPOINT ["Bash","-DBash.security.egd=file:/dev/./urandom","-jar","/app.jar","--spring.profiles.active=prd"]
