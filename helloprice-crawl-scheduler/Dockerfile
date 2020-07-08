FROM java:8
WORKDIR /

RUN mkdir -p /app
COPY ./ /app/

RUN echo "Asia/Seoul" > /etc/timezone

CMD java \
    -jar \
    -Dspring.profiles.active=deploy \
    -Dspring.config.additional-location=file:/app/ \
    -Xms512m \
    -Xmx512m \
    -XX:NewSize=256m \
    -XX:MaxNewSize=256m \
    -XX:MetaspaceSize=50M \
    /app/$(ls /app | grep -E '.*\.jar')

