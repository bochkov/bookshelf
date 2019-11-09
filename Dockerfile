FROM openjdk:jre-alpine
RUN apk update && \
  apk add ca-certificates wget && \
  update-ca-certificates && \
  wget -O /usr/local/bin/dumb-init https://github.com/Yelp/dumb-init/releases/download/v1.2.2/dumb-init_1.2.2_amd64 && \
  chmod +x /usr/local/bin/dumb-init
WORKDIR /opt
ADD web/build/libs/bookshelf-web-2.5.jar bookshelf.jar
EXPOSE 8080
ENTRYPOINT ["/usr/local/bin/dumb-init", "--"]
CMD ["/bin/sh", "-c", "java $JAVA_OPTS -Dfile.encoding=UTF-8 -Djava.security.egd=file:/dev/./urandom -jar bookshelf.jar --host=mongo"]
