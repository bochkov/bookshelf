FROM java:8
VOLUME /tmp
WORKDIR /tmp
WORKDIR /opt
ADD bookshelf.jar /opt/bookshelf.jar
ENTRYPOINT ["java", "-Xms128m", "-Xmx512g", "-jar", "bookshelf.jar"]
