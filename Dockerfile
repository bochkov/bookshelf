FROM eclipse-temurin:25-jre-alpine
RUN apk update && \
  apk --no-cache add ca-certificates wget && \
  update-ca-certificates
WORKDIR /opt
ADD web/build/libs/web-3.0.jar bookshelf.jar
EXPOSE 8080
ENTRYPOINT ["/bin/sh", "-c", "java $JAVA_OPTS -Dfile.encoding=UTF-8 -jar bookshelf.jar --host=mongo"]
