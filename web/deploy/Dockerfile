FROM java:alpine
VOLUME /tmp
WORKDIR /tmp
WORKDIR /opt
ADD bookshelf.jar /opt/bookshelf.jar
ENTRYPOINT ["java", "-Xmx128m", "-Xss1m", "-Dfile.encoding=UTF-8", "-Djava.security.egd=file:/dev/./urandom", "-jar", "bookshelf.jar", "--host=mongo"]
