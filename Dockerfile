# openjdk 8u222
FROM openjdk:8u222-jre

MAINTAINER opsli.com
LABEL version=V1.3.3
LABEL description=OPSLI-快速开发平台
LABEL qqGroup=724850675

# 设置环境常量
ENV WORKSPACE=/usr/local/opsli/opsli-boot \
    LOG_PATH=/var/log \
    UPLOAD_PATH=/upload/files \
    TZ=Asia/Shanghai \
    JAR_NAME=opsli-starter-1.3.3.jar

# 工作目录
WORKDIR ${WORKSPACE}

# 日志输出
RUN ["echo","OPSLI 快速开发平台 building..."]

# 创建日志目录
RUN mkdir -p ${LOG_PATH}

# 创建文件上传目录
RUN mkdir -p ${UPLOAD_PATH}

# 拷贝Jar
ADD ./think-modules/target/${JAR_NAME} ./

# 切换为上海时区
RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime \
    && echo $TZ > /etc/timezone

# 启动镜像
ENTRYPOINT java -jar ${JAR_NAME} -Dfile.encoding=utf-8 -Djava.security.egd=file:/dev/./urandom --spring.profiles.active=docker --log.path=${LOG_PATH} --opsli.web.upload-path=${UPLOAD_PATH}
CMD ["-Xmx2048m -Xms2048m  -XX:+UseG1GC -XX:MaxGCPauseMillis=200"]

# 暴露端口
EXPOSE 8080
