FROM java:8
VOLUME /tmp
WORKDIR /tmp
WORKDIR /opt
ADD bookshelf.jar /opt/bookshelf.jar
ENTRYPOINT ["java", "-Xms64m", "-Xmx128g", "-jar", "bookshelf.jar"]
