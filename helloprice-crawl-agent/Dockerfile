FROM java:8
WORKDIR /

ADD . /app

RUN echo "Asia/Seoul" > /etc/timezone

CMD java \
    -jar \
    -Dspring.profiles.active=deploy \
    -Dspring.config.additional-location=file:./app/ \
    -Xms1024m \
    -Xmx1024m \
    -XX:NewSize=512m \
    -XX:MaxNewSize=512m \
    -XX:MetaspaceSize=100M \
    ./app/$(ls .| grep -E '.*\.jar')

