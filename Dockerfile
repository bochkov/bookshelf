FROM eclipse-temurin:17-jre-alpine
RUN apk update && \
  apk add ca-certificates wget && \
  update-ca-certificates
WORKDIR /opt
ADD web/build/libs/web-3.0.jar bookshelf.jar
EXPOSE 8080
ENTRYPOINT ["/bin/sh", "-c", "java $JAVA_OPTS -Dfile.encoding=UTF-8 -Djava.security.egd=file:/dev/./urandom -jar bookshelf.jar --host=mongo"]
