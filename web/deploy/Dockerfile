FROM sb/java
VOLUME /tmp
WORKDIR /tmp
WORKDIR /opt
ADD bookshelf.jar /opt/bookshelf.jar
ENTRYPOINT ["java", "-Xms32m", "-Xmx64m", "-jar", "bookshelf.jar"]
