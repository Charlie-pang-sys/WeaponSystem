FROM openjdk:8-jre-alpine

COPY app/order-system-1.0-SNAPSHOT.jar order-system.jar

# 设置时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

EXPOSE 8080

ENTRYPOINT ["java","-Xms100m","-Xmx200m", "-jar", "order-system.jar"]