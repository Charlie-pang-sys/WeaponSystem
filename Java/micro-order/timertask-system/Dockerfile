FROM openjdk:8-jdk-alpine

COPY timertask-system-1.0-SNAPSHOT.jar timertask-system.jar

# 设置时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

EXPOSE 8083

ENTRYPOINT ["java","-Xms100m","-Xmx200m", "-jar", "timertask-system.jar"]