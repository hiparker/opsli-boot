# openjdk 8u222
FROM openjdk:8u222-jre

MAINTAINER opsli.com
LABEL version=v1.3.3
LABEL description=OPSLI-快速开发平台
LABEL qqGroup=724850675

# 工作目录
WORKDIR /usr/local/opsli/opsli-boot

# 暴露端口
EXPOSE 7000

# 日志输出
RUN ["echo","OPSLI 快速开发平台正在 building..."]

# 创建日志目录
RUN mkdir -p /var/log

# 创建文件上传目录
RUN mkdir -p /upload/files

# 更改文件名称
RUN find ./opsli-starter/target -type f -regex  ".*\.\(jar\)" | xargs -i mv {} ./opsli-starter/target/opsli-starter.jar

# 拷贝Jar
ADD ./opsli-starter/target/opsli-starter.jar ./

# JVM configs deprecated for removal in opsli
ENV JVM_OPTS=" -Xmx2048m -Xms2048m  -XX:+UseG1GC -XX:MaxGCPauseMillis=200 " \
    TZ=Asia/Shanghai \

# 切换为上海时区
RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime \
    && echo $TZ > /etc/timezone

ENTRYPOINT java ${JVM_OPTS} -Dfile.encoding=utf-8 -Djava.security.egd=file:/dev/./urandom -jar opsli-starter.jar


