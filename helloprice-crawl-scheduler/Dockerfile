FROM java:8
WORKDIR /

ADD helloprice-crawl-scheduler.jar helloprice-crawl-scheduler.jar
ADD conf conf

RUN echo "Asia/Seoul" > /etc/timezone

CMD [
    "java \\",
    "-jar \\",
    "-Dspring.profiles.active=deploy \\",
    "-Dspring.config.additional-location=file:./conf/ \\",
    "-Xms512m \\",
    "-Xmx512m \\",
    "-XX:NewSize=256m \\",
    "-XX:MaxNewSize=256m \\",
    "-XX:MetaspaceSize=50M \\",
    "helloprice-crawl-scheduler.jar"
]
