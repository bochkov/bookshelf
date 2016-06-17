FROM sb-java
VOLUME /tmp
WORKDIR /tmp
WORKDIR /opt
ADD bookshelf.jar /opt/bookshelf.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Xms32m", "-Xmx64m", "-Dfile.encoding=UTF-8", "-jar", "bookshelf.jar"]
