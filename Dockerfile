FROM openjdk:16-alpine
RUN apk update && \
  apk add ca-certificates wget && \
  update-ca-certificates
WORKDIR /opt
ADD web/build/libs/web-3.0.jar bookshelf.jar
EXPOSE 8080
CMD ["java", "$JAVA_OPTS", "-Dfile.encoding=UTF-8", "-Djava.security.egd=file:/dev/./urandom -jar", "bookshelf.jar", "--host=mongo"]
