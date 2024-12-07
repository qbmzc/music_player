# 使用官方Java运行时作为基础镜像
FROM openjdk:21-jdk-alpine

# 设置工作目录
WORKDIR /app

# 复制Maven构建的JAR文件
COPY target/*.jar app.jar

# 暴露端口
EXPOSE 8080

# 运行JAR文件
ENTRYPOINT ["java","-jar","app.jar"]