# openjdk 8u222
FROM openjdk:8u222-jre

MAINTAINER opsli.com
LABEL description=OPSLI-快速开发平台
LABEL qqGroup=724850675

# 设置环境常量
ENV TZ=Asia/Shanghai

# 工作目录
WORKDIR /usr/local/opsli/opsli-boot

# 日志输出
RUN ["echo","OPSLI 快速开发平台 building..."]

# 拷贝Jar
COPY ./opsli-starter/target/*.jar ./app.jar

# 切换为上海时区
RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime \
    && echo $TZ > /etc/timezone

# 启动镜像
ENTRYPOINT ["java", "-Dfile.encoding=utf-8", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
CMD ["-Xmx2048m", "-Xms2048m",  "-XX:+UseG1GC", "-XX:MaxGCPauseMillis=200", "--spring.profiles.active=dev"]

# 暴露端口
EXPOSE 7000