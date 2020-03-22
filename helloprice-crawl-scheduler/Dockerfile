FROM java:8
WORKDIR /

ADD . .

RUN echo "Asia/Seoul" > /etc/timezone

CMD java \
    -jar \
    -Dspring.profiles.active=deploy \
    -Dspring.config.additional-location=file:. \
    -Xms512m \
    -Xmx512m \
    -XX:NewSize=256m \
    -XX:MaxNewSize=256m \
    -XX:MetaspaceSize=50M \
    ./$(ls .| grep -E '.*\.jar')

