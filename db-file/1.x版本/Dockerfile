FROM mysql:8.0.19

MAINTAINER opsli.com
LABEL version=V1.3.3
LABEL description=OPSLI-快速开发平台
LABEL qqGroup=724850675

ENV TZ=Asia/Shanghai

# 切换为上海时区
RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime \
    && echo $TZ > /etc/timezone

COPY ./opsli-boot.sql /docker-entrypoint-initdb.d