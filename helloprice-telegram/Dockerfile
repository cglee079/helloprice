FROM java:8
WORKDIR /
ADD helloprice-telegram.jar helloprice-telegram.jar
EXPOSE 8443
RUN echo "Asia/Seoul" > /etc/timezone
CMD ["java", "-jar", "-Dspring.profiles.active=deploy", "-Dspring.config.additional-location=file:./conf/", "-Xms1024m", "-Xmx1024m", "-XX:NewSize=512m", "-XX:MaxNewSize=512m", "-XX:MetaspaceSize=100M", "helloprice-telegram.jar"]
