FROM java:8
WORKDIR /
ADD podo-helloprice-telegram.jar podo-helloprice-telegram.jar
EXPOSE 8443
CMD ["java", "-jar", "-Dspring.profiles.active=deploy", "-Xms2048m", "-Xmx2048m", "-XX:NewSize=1024m", "-XX:MaxNewSize=1024m", "-XX:MetaspaceSize=100M", "podo-helloprice-telegram.jar"]
