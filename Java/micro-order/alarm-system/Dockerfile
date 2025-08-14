FROM openjdk:8-jdk-alpine

COPY alarm-system-1.0-SNAPSHOT.jar alarm-system.jar

# 设置时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

EXPOSE 8082

ENTRYPOINT ["java","-Xms100m","-Xmx200m", "-jar", "alarm-system.jar"]