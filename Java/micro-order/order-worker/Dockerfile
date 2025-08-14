FROM openjdk:8-jdk-alpine

COPY order-worker-1.0-SNAPSHOT.jar order-worker.jar

# 设置时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

EXPOSE 8081

ENTRYPOINT ["java","-Xms100m","-Xmx200m", "-jar", "order-worker.jar"]