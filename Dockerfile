# openjdk 8u222
FROM openjdk:8u222-jre

MAINTAINER opsli.com
LABEL version=v1.3.3
LABEL description=OPSLI-快速开发平台
LABEL qqGroup=724850675

# 工作目录
WORKDIR /usr/local/opsli/opsli-boot

# 日志输出
RUN ["echo","OPSLI 快速开发平台正在 building..."]

# 切换为上海时区
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

# 创建日志目录
RUN mkdir -p /var/log

# 创建文件上传目录
RUN mkdir -p /upload/files



